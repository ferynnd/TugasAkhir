package dev.ferynnd.tugasakhir.helper

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mlkit.vision.pose.PoseLandmark
import dev.ferynnd.tugasakhir.data.model.ExerciseResult

class TypeOfExercise(landmarks: List<NormalizedLandmark>) : BodyPartAngle(landmarks) {

//    fun process(
//        type: ExerciseType,
//        counter: Int,
//        status: Boolean
//    ): ExerciseResult {
//        return when (type) {
//            ExerciseType.PUSH_UP -> pushUp(counter, status)
//            ExerciseType.SQUAT -> squat(counter, status)
//            ExerciseType.SIT_UP -> TODO()
//        }
//    }

    private fun pushUp(counter: Int, status: Boolean): ExerciseResult {
        val avgArmAngle = (angleLeftArm() + angleRightArm()) / 2

        var reps = counter
        var isUp = status

        if (isUp && avgArmAngle < 75) {
            reps++
            isUp = false
        } else if (!isUp && avgArmAngle > 165) {
            isUp = true
        }

        return ExerciseResult(reps, isUp)
    }


   private fun squat(counter: Int, status: Boolean): ExerciseResult {
        val avgLegAngle = (angleLeftLeg() + angleRightLeg()) / 2

        var reps = counter
        var isUp = status

        if (isUp && avgLegAngle < 110) {
            reps++
            isUp = false
        } else if (!isUp && avgLegAngle > 165) {
            isUp = true
        }

        return ExerciseResult(reps, isUp)
    }



}