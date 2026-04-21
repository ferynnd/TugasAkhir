package dev.ferynnd.tugasakhir.helper

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import kotlin.math.abs
import kotlin.math.atan2

object PoseMath {

    // Fungsi untuk menghitung sudut berdasarkan 3 titik
    fun calculateAngle(
        first: Point,
        mid: Point,
        end: Point
    ): Double {
        // Hitung sudut antara dua vektor:
        // v1 = mid → end
        // v2 = mid → first
        var angle = Math.toDegrees((atan2(end.y - mid.y, end.x - mid.x) - atan2(first.y - mid.y, first.x - mid.x)).toDouble())
        // Ambil nilai absolut supaya tidak negatif
        angle = abs(angle)
        // Jika sudut lebih dari 180°, ubah ke sudut terkecil
        if (angle > 180.0) {
            // misal 360 - 300 = 60°
            // kalau sudut lebih dari setengah lingkaran
            // ambil sisi satunya (yang lebih kecil)
            angle = 360.0 - angle
        }
        // Return sudut dalam derajat (0° - 180°)
        return angle
    }
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

