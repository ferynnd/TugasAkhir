package dev.ferynnd.tugasakhir.helper.exercise

import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import dev.ferynnd.tugasakhir.helper.BodyPartAngle
import dev.ferynnd.tugasakhir.helper.ExerciseEvaluation
import dev.ferynnd.tugasakhir.helper.ExerciseState

class RepCounter(
    landmarks: List<NormalizedLandmark>,
    private val calibration: BodyCalibration = BodyCalibration()
) : BodyPartAngle(landmarks) {

    fun countPushUp(counter: Int, state: ExerciseState): ExerciseEvaluation {
        val elbow    = (angleLeftArm() + angleRightArm()) / 2
        val elbowOffset = if (calibration.isCalibrated) calibration.elbowOffset else 0.0
        val elbowTop    = 150.0 + elbowOffset
        val elbowBottom = 100.0 + elbowOffset

        var reps = counter
        var newState = state

        when (state) {
            ExerciseState.TOP -> {
                if (elbow < 140.0 + elbowOffset) newState = ExerciseState.DESCENDING
            }
            ExerciseState.DESCENDING -> {
                if (elbow <= elbowBottom) newState = ExerciseState.BOTTOM
            }
            ExerciseState.BOTTOM -> {
                if (elbow > elbowBottom) newState = ExerciseState.ASCENDING
            }
            ExerciseState.ASCENDING -> {
                if (elbow >= elbowTop) {
                    reps++
                    newState = ExerciseState.TOP
                }
            }
            else -> {}
        }

        Log.d("REP_PUSHUP", "elbow=${"%.1f".format(elbow)} | state=$state→$newState | reps=$reps")
        return ExerciseEvaluation(reps, newState, true, elbow in 65.0..elbowBottom, null)
    }

    fun countSquat(counter: Int, state: ExerciseState): ExerciseEvaluation {
        val knee = angleKnee()
        val kneeOffset = if (calibration.isCalibrated) calibration.kneeOffset else 0.0
        // Dari gambar: TOP ~160-180°, BOTTOM ~90°
        val standMin = 155.0 + kneeOffset
        val bottomMax = 100.0 + kneeOffset  // ~90° toleransi

        var reps = counter
        var newState = state

        when (state) {
            ExerciseState.TOP        -> if (knee < 145.0 + kneeOffset) newState = ExerciseState.DESCENDING
            ExerciseState.DESCENDING -> if (knee <= bottomMax)          newState = ExerciseState.BOTTOM
            ExerciseState.BOTTOM     -> if (knee > bottomMax)           newState = ExerciseState.ASCENDING
            ExerciseState.ASCENDING  -> if (knee >= standMin) { reps++; newState = ExerciseState.TOP }
            else -> {}
        }

        Log.d("REP_SQUAT", "knee=${"%.1f".format(knee)} | state=$state→$newState | reps=$reps")
        return ExerciseEvaluation(reps, newState, true, knee in 80.0..105.0 + kneeOffset, null)
    }


    fun countSitUp(counter: Int, state: ExerciseState): ExerciseEvaluation {
        val spine       = angleSpine()
        val torso       = angleTorso()
        val spineOffset = if (calibration.isCalibrated) calibration.spineOffset else 0.0
        val spineMin    = 155.0  // berbaring — tidak pakai offset

        var reps     = counter
        var newState = state

        // Dari gambar: BOTTOM=berbaring (spine~160-180°), TOP=torso 60-75°
        when (state) {
            ExerciseState.BOTTOM     -> if (torso < 150.0)         newState = ExerciseState.ASCENDING
            ExerciseState.ASCENDING  -> if (torso in 55.0..80.0)   newState = ExerciseState.TOP
            ExerciseState.TOP        -> if (torso > 85.0)          newState = ExerciseState.DESCENDING
            ExerciseState.DESCENDING -> if (spine >= spineMin) { reps++; newState = ExerciseState.BOTTOM }
            else -> {}
        }

        Log.d("REP_SITUP", "torso=${"%.1f".format(torso)} | spine=${"%.1f".format(spine)} | state=$state→$newState | reps=$reps")
        return ExerciseEvaluation(reps, newState, true, torso in 55.0..80.0, null)
    }
}