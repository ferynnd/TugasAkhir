package dev.ferynnd.tugasakhir.helper.exercise

import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.helper.BodyPartAngle

class CalibrationManager(val exerciseType: ExerciseCode) {

    private val samplesSpine  = mutableListOf<Double>()
    private val samplesKnee   = mutableListOf<Double>()
    private val samplesHip    = mutableListOf<Double>()
    private val samplesElbow  = mutableListOf<Double>()
    private val samplesTorso  = mutableListOf<Double>()

    val requiredSamples = 60        // ✅ 60 sampel = ~3 detik pada 20fps
    private var lastSampleTime = 0L
    private val sampleIntervalMs = 50L  // max 20 sampel/detik

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
        if (!isDone) return BodyCalibration()
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
        samplesSpine.clear(); samplesKnee.clear()
        samplesHip.clear(); samplesElbow.clear(); samplesTorso.clear()
    }

    private fun List<Double>.median(): Double {
        val sorted = this.sorted()
        return if (sorted.size % 2 == 0)
            (sorted[sorted.size / 2 - 1] + sorted[sorted.size / 2]) / 2.0
        else sorted[sorted.size / 2]
    }
}