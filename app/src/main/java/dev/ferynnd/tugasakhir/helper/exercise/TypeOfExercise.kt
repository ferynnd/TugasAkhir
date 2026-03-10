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
        calibration: BodyCalibration = BodyCalibration()
    ): ExerciseEvaluation {

        val knee = angleKnee()
        val spine = angleSpine()
        val torso = angleTorso()

        var reps = counter
        var state = currentState
        var feedback: String? = null

        val spineOffset = if (calibration.isCalibrated) calibration.spineOffset else 0.0
        val kneeOffset  = if (calibration.isCalibrated) calibration.kneeOffset  else 0.0

        val spineMin = 160.0 + spineOffset
        val spineMax = 190.0 + spineOffset
        val kneeMin  = 70.0  + kneeOffset
        val kneeMax  = 110.0 + kneeOffset

        val postureValid =
            knee in kneeMin..kneeMax &&
                    spine in spineMin..spineMax

        val depthValid = torso in 55.0..85.0

        if (state == ExerciseState.WAITING_START) {
            if (postureValid) {
                state = ExerciseState.BOTTOM
                feedback = "Posisi siap, mulai sit-up"
            } else {
                feedback = "Berbaring telentang, tekuk lutut ~90°"
            }
            return ExerciseEvaluation(reps, state, postureValid, false, feedback)
        }

        if (knee !in (kneeMin - 5)..(kneeMax + 5)) {
            feedback = "Jaga lutut tetap ~90°"
        }

        when (state) {

            ExerciseState.WAITING_START -> {
                if (postureValid) state = ExerciseState.BOTTOM
            }

            ExerciseState.BOTTOM -> {
                if (torso < 150) {
                    state = ExerciseState.ASCENDING
                }
            }

            ExerciseState.ASCENDING -> {
                if (torso in 55.0..85.0) {
                    state = ExerciseState.TOP
                }
            }

            ExerciseState.TOP -> {
                if (torso > 90) {
                    state = ExerciseState.DESCENDING
                }
            }

            ExerciseState.DESCENDING -> {
                if (spine >= spineMin) {
                    reps++ // Rep dihitung murni dari state machine
                    state = ExerciseState.BOTTOM
                    if (!depthValid) feedback = "Angkat torso lebih tinggi lain kali (60–75°)"
                }
            }
        }

        Log.d(
            "TypeOfExercise",
            "SitUp | Knee=$knee | Spine=$spine | Torso=$torso | State=$state | Reps=$reps | Feedback=$feedback"
        )

        return ExerciseEvaluation(reps, state, postureValid, depthValid, feedback)
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
        calibration: BodyCalibration = BodyCalibration()
    ): ExerciseEvaluation {

        var reps = counter
        var state = currentState
        var feedback: String? = null

        val knee = angleKnee()
        val hip = angleLeftLeg()
        val torso = angleTorso()
        val spine = angleSpine()

        val kneeOffset  = if (calibration.isCalibrated) calibration.kneeOffset  else 0.0
        val hipOffset   = if (calibration.isCalibrated) calibration.hipOffset   else 0.0
        val spineOffset = if (calibration.isCalibrated) calibration.spineOffset else 0.0

        // Threshold berdiri tegak — disesuaikan offset tubuh
        val standKneeMin = 155.0 + kneeOffset
        val standKneeMax = 185.0 + kneeOffset
        val standHipMin  = 155.0 + hipOffset
        val standHipMax  = 185.0 + hipOffset

        val postureValid =
            knee in standKneeMin..standKneeMax &&
                    hip  in standHipMin..standHipMax  &&
                    torso in 75.0..105.0


        val depthValid =
            knee in 80.0..105.0 &&
                    hip in 80.0..105.0

        if (state == ExerciseState.WAITING_START) {
            if (postureValid) {
                state = ExerciseState.TOP
                feedback = "Posisi siap, mulai squat"
            } else {
                feedback = "Berdiri tegak, kaki selebar bahu"
            }
            return ExerciseEvaluation(reps, state, postureValid, false, feedback)
        }

        if (spine < 155.0 + spineOffset && spine > 10) {
            feedback = "Jaga punggung tetap lurus"
        }

        if (state == ExerciseState.DESCENDING && knee > 120.0 + kneeOffset) {
            feedback = "Turun lebih dalam, dorong pinggul ke belakang"
        }


        when (state) {

            ExerciseState.WAITING_START -> {
                if (postureValid) state = ExerciseState.TOP
            }

            ExerciseState.TOP -> {
                if (knee < 150 + kneeOffset) {
                    state = ExerciseState.DESCENDING
                }
            }

            ExerciseState.DESCENDING -> {
                if (knee <= 105 + kneeOffset) {
                    state = ExerciseState.BOTTOM
                }
            }

            ExerciseState.BOTTOM -> {
                if (knee > 105 + kneeOffset) {
                    state = ExerciseState.ASCENDING
                }
            }

            ExerciseState.ASCENDING -> {
                if (knee >= standKneeMin) {
                    reps++ // Rep dihitung murni dari state machine
                    state = ExerciseState.TOP
                    if (!depthValid) feedback = "Squat lebih dalam lain kali (lutut ~90°)"
                }
            }
        }

        Log.d(
            "TypeOfExercise",
            "Squat | Knee=$knee | Hip=$hip | Torso=$torso | Spine=$spine | State=$state | Reps=$reps | Feedback=$feedback"
        )

        return ExerciseEvaluation(reps, state, postureValid, depthValid, feedback)
    }
}