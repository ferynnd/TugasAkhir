package dev.ferynnd.tugasakhir.helper.exercise

import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import dev.ferynnd.tugasakhir.helper.BodyPartAngle
import dev.ferynnd.tugasakhir.helper.ExerciseState

data class CorrectionResult(
    val feedback: String?,
    val isCorrect: Boolean,
    val correctionType: CorrectionType = CorrectionType.NONE
)

enum class CorrectionType {
    NONE,
    SPINE_NOT_STRAIGHT,     // punggung tidak lurus
    HAND_TOO_WIDE,          // tangan terlalu lebar
    KNEE_WRONG_ANGLE,       // lutut salah sudut
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
        val spine    = angleSpine() // Posisi tulang belakang
        val shoulder = (angleLeftShoulder() + angleRightShoulder()) / 2 // Posisi Bahu
        val elbow    = (angleLeftArm() + angleRightArm()) / 2 // Posisi Siku
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

        // 2. Bahu terlalu lebar (>90° = tidak ideal)
        // bahu ideal = 45° saat turun
        if (isActiveState && shoulder > 75.0) {
            Log.d("FORM_PUSHUP", "❌ Shoulder terlalu lebar: $shoulder")
            return CorrectionResult(
                feedback = "Rapatkan siku ke tubuh, bahu ~45°",
                isCorrect = false,
                correctionType = CorrectionType.HAND_TOO_WIDE
            )
        }

        // 3. Kedalaman kurang saat BOTTOM (siku harus ~90°)
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

        val spineRaw = angleSpine() //  Posisi tulang belakang
        val kneeRaw = angleKnee() // Posisi Lutut
        val torso = angleTorso() // Posisi bagian tubuh tengah Dada + perut + punggung

        val spineOffset = if (calibration.isCalibrated) calibration.spineOffset else 0.0 // selisih sudut bahu
        val kneeOffset = if (calibration.isCalibrated) calibration.kneeOffset else 0.0 // selisih sudut lutut

        val spine = spineRaw + spineOffset // sudut bahu yang sudah disesuaikan
        val knee = kneeRaw + kneeOffset // sudut lutut yang sudah disesuaikan

        val isActiveState = state in listOf(
            ExerciseState.DESCENDING,
            ExerciseState.BOTTOM,
            ExerciseState.ASCENDING
        )

        Log.d(
            "FORM_SQUAT",
            """
                state=$state
                spine=${"%.1f".format(spine)}
                knee=${"%.1f".format(knee)}
                torso=${"%.1f".format(torso)}
                """.trimIndent()
                )

        // Punggung membungkuk
        if (spine < 70) {
            Log.d("FORM_SQUAT", "❌ Spine membungkuk")
            return CorrectionResult(
                feedback = "Tegakkan punggung",
                isCorrect = false,
                correctionType = CorrectionType.SPINE_NOT_STRAIGHT
            )
        }

        // Badan terlalu condong
        // torso = tubuh bagian tengah (dada + perut + punggung)
        if (isActiveState && torso < 30.0) {

            Log.d("FORM_SQUAT", "❌ Torso terlalu condong")

            return CorrectionResult(
                feedback = "Jangan terlalu condong ke depan",
                isCorrect = false,
                correctionType = CorrectionType.SPINE_NOT_STRAIGHT
            )
        }

        // Depth kurang
        if (state == ExerciseState.BOTTOM && knee > 100.0) {

            Log.d("FORM_SQUAT", "❌ Squat kurang dalam")

            return CorrectionResult(
                feedback = "Turun lebih dalam",
                isCorrect = false,
                correctionType = CorrectionType.DEPTH_NOT_ENOUGH
            )
        }

        Log.d("FORM_SQUAT", "✅ Form benar")

        return CorrectionResult(
            feedback = null,
            isCorrect = true
        )
    }

    // ══════════════════════════════════════════
    //  SIT-UP CORRECTIONS
    // ══════════════════════════════════════════
    fun analyzeSitUp(state: ExerciseState): CorrectionResult {
        val spine = angleSpine()
        val knee  = angleKnee()
        val torso = angleTorso()

        Log.d("FORM_SITUP",
            "state=$state | " +
                    "spine=${"%.1f".format(spine)} |" +
                    " knee=${"%.1f".format(knee)} |" +
                    " torso=${"%.1f".format(torso)}"
        )

        // Lutut: berbaring ~40-80°, saat TOP ~80-100° (lutut ±90°)
        val kneeRange = when (state) {
            ExerciseState.BOTTOM,
            ExerciseState.ASCENDING,
            ExerciseState.DESCENDING -> 40.0..100.0  // toleransi lebar saat bergerak
            ExerciseState.TOP        -> 70.0..115.0  // saat fase atas lutut ~90°
            else                     -> 40.0..115.0
        }

        if (knee !in kneeRange) {
            Log.d("FORM_SITUP", "❌ Lutut salah: $knee (harus $kneeRange)")
            return CorrectionResult(
                feedback = "Tekuk lutut ~90°",
                isCorrect = false,
                correctionType = CorrectionType.KNEE_WRONG_ANGLE
            )
        }

        // Torso tidak cukup naik saat TOP (60–75°)
        if (state == ExerciseState.TOP && torso > 75.0) {
            Log.d("FORM_SITUP", "❌ Torso kurang naik: $torso saat TOP")
            return CorrectionResult(
                feedback = "Angkat torso lebih tinggi, 60-75°",
                isCorrect = false,
                correctionType = CorrectionType.DEPTH_NOT_ENOUGH
            )
        }

        // Punggung tidak rebah saat BOTTOM (155°)
        if (state == ExerciseState.BOTTOM && spine < 155.0) {
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