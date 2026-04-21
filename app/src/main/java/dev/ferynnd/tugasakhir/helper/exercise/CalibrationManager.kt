package dev.ferynnd.tugasakhir.helper.exercise

import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.helper.BodyPartAngle

// mengumpulkan data sudut tubuh selama beberapa detik
// untuk menentukan baseline (posisi normal user)
class CalibrationManager(val exerciseType: ExerciseCode) {

    private val samplesSpine  = mutableListOf<Double>()
    private val samplesKnee   = mutableListOf<Double>()
    private val samplesHip    = mutableListOf<Double>()
    private val samplesElbow  = mutableListOf<Double>()
    private val samplesTorso  = mutableListOf<Double>()

    val requiredSamples = 60        // 60 sampel = ~3 detik pada 20fps
    private var lastSampleTime = 0L // L disini tipe Long
    private val sampleIntervalMs = 50L  // 50 ms = 0.05 detik

    // 1 detik / 0.05 detik = 20 sample per detik
    // 60 sample / 20 sample per detik = 3 detik

    val progress: Int get() = minOf(samplesSpine.size, requiredSamples)
    val isDone: Boolean get() = samplesSpine.size >= requiredSamples

    fun addSample(landmarks: List<NormalizedLandmark>) {
        if (isDone) return

        val now = System.currentTimeMillis()
        if (now - lastSampleTime < sampleIntervalMs) return
        lastSampleTime = now

        val angle = BodyPartAngle(landmarks)
        samplesSpine.add(angle.angleSpine())
        samplesKnee.add(angle.angleKnee())
        samplesHip.add(angle.angleLeftLeg())
        samplesElbow.add((angle.angleLeftArm() + angle.angleRightArm()) / 2)
        samplesTorso.add(angle.angleTorso())

        Log.d("CALIB", "Sample ${samplesSpine.size}/$requiredSamples")
    }

    fun buildCalibration(): BodyCalibration {
        if (!isDone) return BodyCalibration() // return default (180° semua)
        return when (exerciseType) {

            // Baseline berdiri → spine & knee & hip harusnya ~170–180°
            ExerciseCode.SQUAT -> BodyCalibration(
                baselineSpine = samplesSpine.median(),
                baselineKnee  = samplesKnee.median(),
                baselineHip   = samplesHip.median(),
                isCalibrated  = true
            )

            // Baseline plank → elbow & spine
            ExerciseCode.PUSH_UP -> BodyCalibration(
                baselineSpine  = samplesSpine.median(),
                baselineElbow  = samplesElbow.median(),
                isCalibrated   = true
            )

            // Baseline berbaring → spine & knee
            ExerciseCode.SIT_UP -> BodyCalibration(
                baselineSpine = samplesSpine.median(),
                baselineKnee  = samplesKnee.median(),
                baselineTorso = samplesTorso.median(),
                isCalibrated  = true
            )
        }
    }

    fun reset() {
        samplesSpine.clear();
        samplesKnee.clear()
        samplesHip.clear();
        samplesElbow.clear();
        samplesTorso.clear()
    }

    private fun List<Double>.median(): Double {
        val sorted = this.sorted() // urutkan daftar
        return if (sorted.size % 2 == 0) // jika jumlah elemen genap
            (sorted[sorted.size / 2 - 1] + sorted[sorted.size / 2]) / 2.0 // rata-rata dua nilai tengah
        else sorted[sorted.size / 2] // jika jumlah elemen ganjil, ambil nilai tengah
    }
}