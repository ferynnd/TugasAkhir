package dev.ferynnd.tugasakhir.helper.exercise

data class BodyCalibration(
    val baselineSpine:  Double = 180.0,
    val baselineKnee:   Double = 180.0,
    val baselineHip:    Double = 180.0,
    val baselineElbow:  Double = 180.0,
    val baselineTorso:  Double = 180.0,
    val isCalibrated:   Boolean = false
) {
    // Offset = selisih dari nilai ideal masing-masing posisi
    val spineOffset:  Double get() = baselineSpine  - 180.0
    val kneeOffset:   Double get() = baselineKnee   - 180.0
    val hipOffset:    Double get() = baselineHip     - 180.0
    val elbowOffset:  Double get() = baselineElbow   - 180.0

    // Sit-up: baseline torso saat berbaring (bukan 180°, tapi ~170–180°)
    val torsoBaselineLying: Double get() = baselineTorso
}
