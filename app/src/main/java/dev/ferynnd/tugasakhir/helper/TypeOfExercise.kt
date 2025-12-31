package dev.ferynnd.tugasakhir.helper

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mlkit.vision.pose.PoseLandmark

class TypeOfExercise(landmarks: List<NormalizedLandmark>) : BodyPartAngle(landmarks) {

    fun pushUp(counter: Int, status: Boolean): ExerciseResult {
        val avgArmAngle = (angleLeftArm() + angleRightArm()) / 2
        var currentCounter = counter
        var currentStatus = status

        if (currentStatus) {
            if (avgArmAngle < 70) {
                currentCounter++
                currentStatus = false
            }
        } else {
            if (avgArmAngle > 160) {
                currentStatus = true
            }
        }
        return ExerciseResult(currentCounter, currentStatus)
    }

    fun squat(counter: Int, status: Boolean): ExerciseResult {
        val avgLegAngle = (angleLeftLeg() + angleRightLeg()) / 2
        var currentCounter = counter
        var currentStatus = status

        if (currentStatus) {
            if (avgLegAngle < 120) {
                currentCounter++
                currentStatus = false
            }
        } else {
            if (avgLegAngle > 160) {
                currentStatus = true
            }
        }
        return ExerciseResult(currentCounter, currentStatus)
    }

    fun pullUp(counter: Int, status: Boolean): ExerciseResult {
        val nose = getPoint(PoseLandmark.NOSE)
        val leftElbowY = getPoint(PoseLandmark.LEFT_ELBOW).y
        val rightElbowY = getPoint(PoseLandmark.RIGHT_ELBOW).y
        val avgShoulderY = (leftElbowY + rightElbowY) / 2

        var currentCounter = counter
        var currentStatus = status

        // Catatan: Di koordinat layar Android, Y makin ke bawah makin besar
        if (currentStatus) {
            if (nose.y > avgShoulderY) { // Turun
                currentCounter++
                currentStatus = false
            }
        } else {
            if (nose.y < avgShoulderY) { // Naik
                currentStatus = true
            }
        }
        return ExerciseResult(currentCounter, currentStatus)
    }

}