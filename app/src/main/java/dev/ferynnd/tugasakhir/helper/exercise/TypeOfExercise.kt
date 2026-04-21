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
        postureGate: PostureGate // Reset jika postur tidak valid
    ): ExerciseEvaluation {

        val validator  = PostureValidator(landmarks, calibration)
        val repCounter = RepCounter(landmarks, calibration)
        val correction = FormCorrection(landmarks, calibration)

        val isValidPosture = validator.isPushUpPosture()

        if (currentState == ExerciseState.WAITING_START) {
            postureGate.reset()
            return if (isValidPosture) {
                ExerciseEvaluation(counter, ExerciseState.TOP, true, false,
                    "Posisi siap, mulai push-up", isCorrect = true)
            } else {
                ExerciseEvaluation(counter, ExerciseState.WAITING_START, false, false,
                    "Ambil posisi push-up yang benar..", isCorrect = false)
            }
        }

        if (!isValidPosture) {
            postureGate.reset()
            return ExerciseEvaluation(counter, ExerciseState.WAITING_START, false, false,
                "Kembali ke posisi push-up", isCorrect = false)
        }

        val correctionResult = correction.analyzePushUp(currentState) // return feedback dan isCorrect
        Log.d("PUSHUP_CORRECTION",
            "feedback=${correctionResult.feedback} |" +
                    " isCorrect=${correctionResult.isCorrect} |" +
                    " type=${correctionResult.correctionType}")

        val result = if ( correctionResult.isCorrect) {
            repCounter.countPushUp(counter, currentState) // return reps dan newState
        } else {
            ExerciseEvaluation(
                reps = counter,
                state = currentState,
                postureValid = false,
                depthValid = false,
                feedback = correctionResult.feedback,
                isCorrect = false
            )
        }
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

        if (currentState == ExerciseState.WAITING_START) {
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

        // Jangan reset saat state sedang ASCENDING / TOP / DESCENDING
        // PostureGate hanya relevan saat di BOTTOM (istirahat antar rep)
        val isActiveMovement = currentState in listOf(
            ExerciseState.ASCENDING,
            ExerciseState.TOP,
            ExerciseState.DESCENDING
        )

        if (!isActiveMovement) {
            val shouldReset = postureGate.update(isValidPosture) // return true jika reset
            if (shouldReset) {
                postureGate.reset()
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

        // Jika postur hilang total (misal tiba-tiba berdiri), balik ke WAITING_START
        if (!isValidPosture) {
            postureGate.reset()
            return ExerciseEvaluation(counter, ExerciseState.WAITING_START, false, false,
                "Kembali ke posisi berbaring", isCorrect = false)
        }

        val correctionResult = correction.analyzeSitUp(currentState) // return feedback dan isCorrect

        val result = if(correctionResult.isCorrect) {
            repCounter.countSitUp(counter, currentState) // return reps dan newState
        } else {
            ExerciseEvaluation(
                reps = counter,
                state = currentState,
                postureValid = true,
                depthValid = false,
                feedback = correctionResult.feedback,
                isCorrect = false
            )
        }
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

        if (currentState == ExerciseState.WAITING_START) {
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

        // 2. Hard Reset jika tiba-tiba keluar dari posisi squat (misal jalan menjauh)
        if (!isValidPosture) {
            postureGate.reset()
            return ExerciseEvaluation(counter, ExerciseState.WAITING_START, false, false,
                "Kembali ke posisi berdiri tegak", isCorrect = false)
        }

        val correctionResult = correction.analyzeSquat(currentState) // return feedback dan isCorrect

        Log.d(
            "SQUAT_CORRECTION",
            "feedback=${correctionResult.feedback} |" +
                    " isCorrect=${correctionResult.isCorrect} |" +
                    " type=${correctionResult.correctionType}"
        )

        val result = if (correctionResult.isCorrect) {
            repCounter.countSquat(counter, currentState) // return reps dan newState
        } else {
            ExerciseEvaluation(
                reps = counter,
                state = currentState,
                postureValid = true,
                depthValid = false,
                feedback = correctionResult.feedback,
                isCorrect = false
            )
        }

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