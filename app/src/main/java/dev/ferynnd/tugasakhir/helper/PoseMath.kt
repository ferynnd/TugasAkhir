package dev.ferynnd.tugasakhir.helper

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import kotlin.math.abs
import kotlin.math.atan2

object PoseMath {

    // Fungsi untuk menghitung sudut antara 3 titik (seperti fungsi Python Anda)
    fun calculateAngle(
        first: Point,
        mid: Point,
        end: Point
    ): Double {
        var angle = Math.toDegrees((atan2(end.y - mid.y, end.x - mid.x) - atan2(first.y - mid.y, first.x - mid.x)).toDouble())
        angle = abs(angle)
        if (angle > 180.0) {
            angle = 360.0 - angle
        }
        return angle
    }

    // Helper class untuk koordinat
    data class Point(val x: Float, val y: Float)
}


enum class ExerciseState {
    WAITING_START,
    TOP,
    DESCENDING,
    BOTTOM,
    ASCENDING
}

data class ExerciseEvaluation(
    val reps: Int,
    val state: ExerciseState,
    val postureValid: Boolean,
    val depthValid: Boolean,
    val feedback: String?,
    val isCorrect: Boolean = true
)

