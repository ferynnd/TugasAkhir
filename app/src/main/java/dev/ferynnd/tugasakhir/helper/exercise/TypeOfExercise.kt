package dev.ferynnd.tugasakhir.helper.exercise

import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import dev.ferynnd.tugasakhir.helper.BodyPartAngle
import dev.ferynnd.tugasakhir.helper.ExerciseEvaluation
import dev.ferynnd.tugasakhir.helper.ExerciseState

class TypeOfExercise(landmarks: List<NormalizedLandmark>) : BodyPartAngle(landmarks) {

    /**
     * PUSH-UP
     */
    fun evaluatePushUp(
        counter: Int,
        currentState: ExerciseState,
        calibration: BodyCalibration = BodyCalibration(),
        postureGate: PostureGate
    ): ExerciseEvaluation {

        val validator  = PostureValidator(landmarks, calibration)
        val repCounter = RepCounter(landmarks, calibration)
        val correction = FormCorrection(landmarks, calibration)

        val isValidPosture = validator.isPushUpPosture()

        // ✅ Log nilai mentah semua sudut
        val spine    = angleSpine()
        val shoulder = (angleLeftShoulder() + angleRightShoulder()) / 2
        val elbow    = (angleLeftArm() + angleRightArm()) / 2
        val spineOffset = if (calibration.isCalibrated) calibration.spineOffset else 0.0

        Log.d("PUSHUP_RAW", """
        |=== PUSH-UP DEBUG ===
        |State       : $currentState
        |Elbow       : ${"%.1f".format(elbow)}°
        |Shoulder    : ${"%.1f".format(shoulder)}°
        |Spine       : ${"%.1f".format(spine)}°
        |SpineOffset : ${"%.1f".format(spineOffset)}°
        |SpineMin    : ${"%.1f".format(150.0 + spineOffset)}° (threshold)
        |isValidPost : $isValidPosture
        |Counter     : $counter
    """.trimMargin())

        if (currentState == ExerciseState.WAITING_START) {
            postureGate.reset()
            Log.d("PUSHUP_STATE", "WAITING_START → isValid=$isValidPosture")
            return if (isValidPosture) {
                ExerciseEvaluation(counter, ExerciseState.TOP, true, false,
                    "Posisi siap, mulai push-up", isCorrect = true)
            } else {
                ExerciseEvaluation(counter, ExerciseState.WAITING_START, false, false,
                    "Ambil posisi plank lurus", isCorrect = false)
            }
        }

        val shouldReset = postureGate.update(isValidPosture)
        Log.d("PUSHUP_GATE", "isValid=$isValidPosture | shouldReset=$shouldReset")

        if (shouldReset) {
            postureGate.reset()
            Log.d("PUSHUP_STATE", "GATE RESET → kembali ke WAITING_START")
            return ExerciseEvaluation(counter, ExerciseState.WAITING_START,
                false, false, "Kembali ke posisi push-up", isCorrect = false)
        }

        val correctionResult = correction.analyzePushUp(currentState)
        Log.d("PUSHUP_CORRECTION", "feedback=${correctionResult.feedback} | isCorrect=${correctionResult.isCorrect} | type=${correctionResult.correctionType}")

        val result = repCounter.countPushUp(counter, currentState)
        Log.d("PUSHUP_REP", "reps=${result.reps} | newState=${result.state}")

        return result.copy(
            feedback = correctionResult.feedback,
            isCorrect = correctionResult.isCorrect
        )
    }

    /**
     * SIT-UP
     * - Posisi awal (BOTTOM): berbaring, spine ~160–190°
     * - Posisi atas (TOP): torso 55–85°
     * - Rep dihitung dari state machine
     */
    fun evaluateSitUp(
        counter: Int,
        currentState: ExerciseState,
        calibration: BodyCalibration = BodyCalibration(),
        postureGate: PostureGate
    ): ExerciseEvaluation {

        val validator  = PostureValidator(landmarks, calibration)
        val repCounter = RepCounter(landmarks, calibration)
        val correction = FormCorrection(landmarks, calibration)

        val isValidPosture = validator.isSitUpPosture()

        val knee  = angleKnee()
        val spine = angleSpine()
        val torso = angleTorso()

        val kneeOffset  = if (calibration.isCalibrated) calibration.kneeOffset else 0.0
        val spineOffset = if (calibration.isCalibrated) calibration.spineOffset else 0.0

        Log.d("SITUP_RAW", """
        |=== SIT-UP DEBUG ===
        |State       : $currentState
        |Knee        : ${"%.1f".format(knee)}°
        |Torso       : ${"%.1f".format(torso)}°
        |Spine       : ${"%.1f".format(spine)}°
        |KneeOffset  : ${"%.1f".format(kneeOffset)}°
        |SpineOffset : ${"%.1f".format(spineOffset)}°
        |isValidPost : $isValidPosture
        |Counter     : $counter
    """.trimMargin())

        if (currentState == ExerciseState.WAITING_START) {
            postureGate.reset()
            Log.d("SITUP_STATE", "WAITING_START → isValid=$isValidPosture")

            return if (isValidPosture) {
                ExerciseEvaluation(
                    counter,
                    ExerciseState.BOTTOM,
                    true,
                    false,
                    "Posisi siap, mulai sit-up",
                    isCorrect = true
                )
            } else {
                ExerciseEvaluation(
                    counter,
                    ExerciseState.WAITING_START,
                    false,
                    false,
                    "Berbaring telentang, lutut ~90°",
                    isCorrect = false
                )
            }
        }

        // ✅ PERBAIKAN UTAMA: Jangan reset saat state sedang ASCENDING / TOP / DESCENDING
        // PostureGate hanya relevan saat di BOTTOM (istirahat antar rep)
        val isActiveMovement = currentState in listOf(
            ExerciseState.ASCENDING,
            ExerciseState.TOP,
            ExerciseState.DESCENDING
        )

        if (!isActiveMovement) {
            val shouldReset = postureGate.update(isValidPosture)
            Log.d("SITUP_GATE", "isValid=$isValidPosture | shouldReset=$shouldReset")

            if (shouldReset) {
                postureGate.reset()
                Log.d("SITUP_STATE", "GATE RESET → kembali WAITING_START")

                return ExerciseEvaluation(
                    counter,
                    ExerciseState.WAITING_START,
                    false,
                    false,
                    "Kembali ke posisi sit-up",
                    isCorrect = false
                )
            }
        }

        val correctionResult = correction.analyzeSitUp(currentState)
        val result = repCounter.countSitUp(counter, currentState)

        return result.copy(
            feedback = correctionResult.feedback,
            isCorrect = correctionResult.isCorrect
        )
    }


    /**
     * SQUAT
     * - Posisi awal (TOP): berdiri tegak, knee & hip ~155–185°
     * - Posisi bawah (BOTTOM): knee & hip ~80–105°
     * - Rep dihitung dari state machine
     */
    fun evaluateSquat(
        counter: Int,
        currentState: ExerciseState,
        calibration: BodyCalibration = BodyCalibration(),
        postureGate: PostureGate
    ): ExerciseEvaluation {

        val validator  = PostureValidator(landmarks, calibration)
        val repCounter = RepCounter(landmarks, calibration)
        val correction = FormCorrection(landmarks, calibration)

        val isValidPosture = validator.isSquatPosture()

        val knee  = angleKnee()
        val hip   = angleLeftLeg()
        val torso = angleTorso()
        val spine = angleSpine()

        val kneeOffset  = if (calibration.isCalibrated) calibration.kneeOffset else 0.0
        val hipOffset   = if (calibration.isCalibrated) calibration.hipOffset else 0.0
        val spineOffset = if (calibration.isCalibrated) calibration.spineOffset else 0.0

        Log.d("SQUAT_RAW", """
        |=== SQUAT DEBUG ===
        |State       : $currentState
        |Knee        : ${"%.1f".format(knee)}°
        |Hip         : ${"%.1f".format(hip)}°
        |Torso       : ${"%.1f".format(torso)}°
        |Spine       : ${"%.1f".format(spine)}°
        |KneeOffset  : ${"%.1f".format(kneeOffset)}°
        |HipOffset   : ${"%.1f".format(hipOffset)}°
        |SpineOffset : ${"%.1f".format(spineOffset)}°
        |isValidPost : $isValidPosture
        |Counter     : $counter
    """.trimMargin())

        if (currentState == ExerciseState.WAITING_START) {

            postureGate.reset()

            Log.d("SQUAT_STATE", "WAITING_START → isValid=$isValidPosture")

            return if (isValidPosture) {
                ExerciseEvaluation(
                    counter,
                    ExerciseState.TOP,
                    true,
                    false,
                    "Posisi siap, mulai squat",
                    isCorrect = true
                )
            } else {
                ExerciseEvaluation(
                    counter,
                    ExerciseState.WAITING_START,
                    false,
                    false,
                    "Berdiri tegak, kaki selebar bahu",
                    isCorrect = false
                )
            }
        }

        val shouldReset = if (currentState == ExerciseState.TOP) {
            postureGate.update(isValidPosture)
        } else {
            false
        }

        Log.d("SQUAT_GATE", "isValid=$isValidPosture | shouldReset=$shouldReset")

        if (shouldReset) {

            postureGate.reset()

            Log.d("SQUAT_STATE", "GATE RESET → kembali WAITING_START")

            return ExerciseEvaluation(
                counter,
                ExerciseState.WAITING_START,
                false,
                false,
                "Kembali ke posisi squat",
                isCorrect = false
            )
        }

        val correctionResult = correction.analyzeSquat(currentState)

        Log.d(
            "SQUAT_CORRECTION",
            "feedback=${correctionResult.feedback} | isCorrect=${correctionResult.isCorrect} | type=${correctionResult.correctionType}"
        )

        val result = repCounter.countSquat(counter, currentState)

        Log.d(
            "SQUAT_REP",
            "reps=${result.reps} | newState=${result.state}"
        )


        return result.copy(
            feedback = correctionResult.feedback,
            isCorrect = correctionResult.isCorrect
        )
    }
}