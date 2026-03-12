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

        val kneeAdjusted = knee + kneeOffset

        // Threshold squat
        val topThreshold = 170.0
        val descendThreshold = 160.0
        val bottomThreshold = 80.0
        val ascendThreshold = 110.0

        var reps = counter
        var newState = state

        when (state) {

            ExerciseState.TOP -> {
                if (kneeAdjusted < descendThreshold) {
                    newState = ExerciseState.DESCENDING
                }
            }

            ExerciseState.DESCENDING -> {
                if (kneeAdjusted <= bottomThreshold) {
                    newState = ExerciseState.BOTTOM
                }
            }

            ExerciseState.BOTTOM -> {
                if (kneeAdjusted > ascendThreshold) {
                    newState = ExerciseState.ASCENDING
                }
            }

            ExerciseState.ASCENDING -> {
                if (kneeAdjusted >= topThreshold) {
                    reps++
                    newState = ExerciseState.TOP
                }
            }

            else -> {}
        }

        val depthOk = kneeAdjusted in 50.0..100.0

        Log.d(
            "REP_SQUAT",
            """
        knee=${"%.1f".format(kneeAdjusted)}
        state=$state -> $newState
        reps=$reps
        depthOk=$depthOk
        """.trimIndent()
        )

        return ExerciseEvaluation(
            reps,
            newState,
            true,
            depthOk,
            null
        )
    }


    fun countSitUp(counter: Int, state: ExerciseState): ExerciseEvaluation {
        val spine = angleSpine()
        val torso = angleTorso()
        val knee  = angleKnee()

        val spineMin = 155.0

        var reps     = counter
        var newState = state

        when (state) {
            // BOTTOM: torso ~94–116°, transisi ke ASCENDING saat turun ke < 95°
            ExerciseState.BOTTOM     -> if (torso < 95.0) newState = ExerciseState.ASCENDING

            // ASCENDING: torso turun dari ~90° ke ~66–73°
            // Dari log: transisi ke TOP saat torso ≤ 75°
            ExerciseState.ASCENDING  -> if (torso <= 75.0) newState = ExerciseState.TOP

            // TOP: tunggu torso mulai naik lagi
            // Dari log: torso di TOP ~58–75°, mulai naik lagi ke ~75–85°
            ExerciseState.TOP        -> if (torso > 82.0) newState = ExerciseState.DESCENDING

            // DESCENDING: turun sampai spine rebah >= 155°
            ExerciseState.DESCENDING -> if (spine >= spineMin) {
                reps++
                newState = ExerciseState.BOTTOM
            }

            else -> {}
        }

        Log.d("REP_SITUP", "torso=${"%.1f".format(torso)} | spine=${"%.1f".format(spine)} | knee=${"%.1f".format(knee)} | state=$state→$newState | reps=$reps")
        return ExerciseEvaluation(reps, newState, true, torso <= 75.0, null)
    }
}