package dev.ferynnd.tugasakhir.helper.exercise


import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import dev.ferynnd.tugasakhir.helper.BodyPartAngle

class PostureValidator(
    landmarks: List<NormalizedLandmark>,
    private val calibration: BodyCalibration = BodyCalibration()
) : BodyPartAngle(landmarks) {

    // ── Cek landmark cukup visible ──────────────────────
    fun isBodyVisible(): Boolean {
        if (landmarks.size < 25) return false
        return landmarks[11].visibility().orElse(0f) > 0.5f && // bahu kiri
                landmarks[12].visibility().orElse(0f) > 0.5f && // bahu kanan
                landmarks[23].visibility().orElse(0f) > 0.5f && // pinggul kiri
                landmarks[24].visibility().orElse(0f) > 0.5f    // pinggul kanan
    }

    // ── Push-up: posisi plank ───────────────────────────
    fun isPushUpPosture(): Boolean {
        if (!isBodyVisible()) {
            Log.d("VALIDATOR", "❌ Body tidak visible")
            return false
        }
        val spine    = angleSpine()
        val shoulder = (angleLeftShoulder() + angleRightShoulder()) / 2
        val elbow    = (angleLeftArm() + angleRightArm()) / 2

        // Posisi plank: spine lurus ~160-180°, shoulder bebas, siku tidak terlipat ekstrim
        val spineOk    = spine in 140.0..185.0
        val shoulderOk = shoulder in 20.0..150.0
        val elbowOk    = elbow >= 50.0

        Log.d("VALIDATOR", "isPushUpPosture: spine=${"%.1f".format(spine)} ok=$spineOk | shoulder=${"%.1f".format(shoulder)} ok=$shoulderOk | elbow=${"%.1f".format(elbow)} ok=$elbowOk → ${spineOk && shoulderOk && elbowOk}")
        return spineOk && shoulderOk && elbowOk
    }

    // ── Squat: posisi berdiri tegak ─────────────────────
    fun isSquatPosture(): Boolean {
        if (!isBodyVisible()) return false
        val knee = angleKnee()
        val hip  = angleLeftLeg()
        val kneeOffset = if (calibration.isCalibrated) calibration.kneeOffset else 0.0
        val hipOffset  = if (calibration.isCalibrated) calibration.hipOffset  else 0.0

        // Posisi berdiri tegak sebelum squat
        val kneeOk = knee in (145.0 + kneeOffset)..(190.0 + kneeOffset)
        val hipOk  = hip  in (145.0 + hipOffset)..(190.0 + hipOffset)

        Log.d("VALIDATOR", "isSquatPosture: knee=${"%.1f".format(knee)} ok=$kneeOk | hip=${"%.1f".format(hip)} ok=$hipOk → ${kneeOk && hipOk}")
        return kneeOk && hipOk
    }

    // ── Sit-up: posisi berbaring ────────────────────────
    fun isSitUpPosture(): Boolean {
        if (!isBodyVisible()) return false
        val spine      = angleSpine()
        val knee       = angleKnee()
        val kneeOffset = if (calibration.isCalibrated) calibration.kneeOffset else 0.0

        // Posisi berbaring: spine ~160-180°, lutut ~90°
        val spineOk = spine in 155.0..195.0
        val kneeOk  = knee in (70.0 + kneeOffset)..(110.0 + kneeOffset)

        Log.d("VALIDATOR", "isSitUpPosture: spine=${"%.1f".format(spine)} ok=$spineOk | knee=${"%.1f".format(knee)} ok=$kneeOk → ${spineOk && kneeOk}")
        return spineOk && kneeOk
    }
}