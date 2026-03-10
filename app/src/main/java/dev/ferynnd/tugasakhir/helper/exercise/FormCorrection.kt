package dev.ferynnd.tugasakhir.helper.exercise

import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import dev.ferynnd.tugasakhir.helper.BodyPartAngle
import dev.ferynnd.tugasakhir.helper.ExerciseState

data class CorrectionResult(
    val feedback: String?,           // teks koreksi (null = semua benar)
    val isCorrect: Boolean,          // true = hijau, false = merah
    val correctionType: CorrectionType = CorrectionType.NONE
)

enum class CorrectionType {
    NONE,
    SPINE_NOT_STRAIGHT,     // punggung tidak lurus
    HAND_TOO_WIDE,          // tangan terlalu lebar
    HAND_TOO_NARROW,        // tangan terlalu sempit
    KNEE_WRONG_ANGLE,       // lutut salah sudut
    FOOT_TOO_NARROW,        // kaki terlalu rapat
    FOOT_TOO_WIDE,          // kaki terlalu lebar
    DEPTH_NOT_ENOUGH        // kedalaman kurang
}

class FormCorrection(
    landmarks: List<NormalizedLandmark>,
    private val calibration: BodyCalibration = BodyCalibration()
) : BodyPartAngle(landmarks) {

    // ══════════════════════════════════════════
    //  PUSH-UP CORRECTIONS
    // ══════════════════════════════════════════
    fun analyzePushUp(state: ExerciseState): CorrectionResult {
        val spine    = angleSpine()
        val shoulder = (angleLeftShoulder() + angleRightShoulder()) / 2
        val elbow    = (angleLeftArm() + angleRightArm()) / 2
        val spineOffset = if (calibration.isCalibrated) calibration.spineOffset else 0.0

        val isActiveState = state in listOf(
            ExerciseState.DESCENDING,
            ExerciseState.BOTTOM,
            ExerciseState.ASCENDING
        )

        Log.d("FORM_PUSHUP", """
        |=== FORM CORRECTION ===
        |State        : $state
        |isActiveState: $isActiveState
        |Spine        : ${"%.1f".format(spine)}° (min=${150.0 + spineOffset})
        |Shoulder     : ${"%.1f".format(shoulder)}° (valid=20..150)
        |Elbow        : ${"%.1f".format(elbow)}° 
    """.trimMargin())

        // 1. Punggung tidak lurus (spine harusnya 140-185° saat plank)
        if (spine < 140.0 || (spine > 185.0 && spine < 350.0)) {
            Log.d("FORM_PUSHUP", "❌ Spine tidak lurus: $spine")
            return CorrectionResult(
                feedback = "Jaga punggung tetap lurus",
                isCorrect = false,
                correctionType = CorrectionType.SPINE_NOT_STRAIGHT
            )
        }

        // 2. Bahu terlalu lebar (>90° dari gambar = tidak ideal)
        // Dari gambar: bahu ideal = 45° saat turun
        if (isActiveState && shoulder > 75.0) {
            Log.d("FORM_PUSHUP", "❌ Shoulder terlalu lebar: $shoulder")
            return CorrectionResult(
                feedback = "Rapatkan siku ke tubuh, bahu ~45°",
                isCorrect = false,
                correctionType = CorrectionType.HAND_TOO_WIDE
            )
        }

        // 3. Kedalaman kurang saat BOTTOM (dari gambar siku harus ~90°)
        if (state == ExerciseState.BOTTOM && elbow > 110.0) {
            Log.d("FORM_PUSHUP", "❌ Depth kurang: elbow=$elbow saat BOTTOM")
            return CorrectionResult(
                feedback = "Turunkan lebih dalam, siku harus ~90°",
                isCorrect = false,
                correctionType = CorrectionType.DEPTH_NOT_ENOUGH
            )
        }

        Log.d("FORM_PUSHUP", "✅ Form benar")
        return CorrectionResult(feedback = null, isCorrect = true)
    }

    // ══════════════════════════════════════════
    //  SQUAT CORRECTIONS
    // ══════════════════════════════════════════

    fun analyzeSquat(state: ExerciseState): CorrectionResult {
        val spine  = angleSpine()
        val knee   = angleKnee()
        val torso  = angleTorso()

        val isActiveState = state in listOf(
            ExerciseState.DESCENDING,
            ExerciseState.BOTTOM,
            ExerciseState.ASCENDING
        )

        Log.d("FORM_SQUAT", "state=$state | spine=${"%.1f".format(spine)} | knee=${"%.1f".format(knee)} | torso=${"%.1f".format(torso)}")

        // 1. Punggung membungkuk
        if (spine < 150.0 && spine > 10) {
            Log.d("FORM_SQUAT", "❌ Spine membungkuk: $spine")
            return CorrectionResult(
                feedback = "Tegakkan punggung, jangan membungkuk",
                isCorrect = false,
                correctionType = CorrectionType.SPINE_NOT_STRAIGHT
            )
        }

        // 2. Torso terlalu condong ke depan
        // Dari gambar: torso ideal saat squat bawah = 60-70°
        if (isActiveState && torso < 50.0) {
            Log.d("FORM_SQUAT", "❌ Torso terlalu condong: $torso")
            return CorrectionResult(
                feedback = "Jangan terlalu condong ke depan",
                isCorrect = false,
                correctionType = CorrectionType.SPINE_NOT_STRAIGHT
            )
        }

        // 3. Kedalaman kurang saat BOTTOM
        // Dari gambar: lutut harus ~90° saat bawah
        if (state == ExerciseState.BOTTOM && knee > 110.0) {
            Log.d("FORM_SQUAT", "❌ Depth kurang: knee=$knee saat BOTTOM")
            return CorrectionResult(
                feedback = "Turun lebih dalam, lutut harus ~90°",
                isCorrect = false,
                correctionType = CorrectionType.DEPTH_NOT_ENOUGH
            )
        }

        Log.d("FORM_SQUAT", "✅ Form benar")
        return CorrectionResult(feedback = null, isCorrect = true)
    }

    // ══════════════════════════════════════════
    //  SIT-UP CORRECTIONS
    // ══════════════════════════════════════════
    fun analyzeSitUp(state: ExerciseState): CorrectionResult {
        val spine = angleSpine()
        val knee  = angleKnee()
        val torso = angleTorso()

        Log.d("FORM_SITUP", "state=$state | spine=${"%.1f".format(spine)} | knee=${"%.1f".format(knee)} | torso=${"%.1f".format(torso)}")

        // 1. Lutut tidak ditekuk ~90° (dari gambar: lutut harus ±90°)
        if (knee !in 70.0..110.0) {
            Log.d("FORM_SITUP", "❌ Lutut salah: $knee (harus 70-110°)")
            return CorrectionResult(
                feedback = "Tekuk lutut ~90°",
                isCorrect = false,
                correctionType = CorrectionType.KNEE_WRONG_ANGLE
            )
        }

        // 2. Torso tidak cukup naik saat TOP
        // Dari gambar: torso harus 60-75° saat posisi atas
        if (state == ExerciseState.TOP && torso > 80.0) {
            Log.d("FORM_SITUP", "❌ Torso kurang naik: $torso saat TOP")
            return CorrectionResult(
                feedback = "Angkat torso lebih tinggi, 60-75°",
                isCorrect = false,
                correctionType = CorrectionType.DEPTH_NOT_ENOUGH
            )
        }

        // 3. Punggung tidak menyentuh lantai saat BOTTOM
        if (state == ExerciseState.BOTTOM && spine < 155.0 && spine > 10) {
            Log.d("FORM_SITUP", "❌ Punggung tidak rebah: spine=$spine")
            return CorrectionResult(
                feedback = "Rebahkan punggung ke lantai sepenuhnya",
                isCorrect = false,
                correctionType = CorrectionType.SPINE_NOT_STRAIGHT
            )
        }

        Log.d("FORM_SITUP", "✅ Form benar")
        return CorrectionResult(feedback = null, isCorrect = true)
    }
}