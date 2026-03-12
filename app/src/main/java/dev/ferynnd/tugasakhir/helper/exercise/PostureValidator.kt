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
        val hipLeft  = angleLeftLeg()
        val hipRight  = angleRightLeg()

        val hip = (hipLeft + hipRight) / 2

        val kneeOffset = if (calibration.isCalibrated) calibration.kneeOffset else 0.0
        val hipOffset  = if (calibration.isCalibrated) calibration.hipOffset else 0.0

        val kneeAdjusted = knee + kneeOffset
        val hipAdjusted  = hip + hipOffset

        // squat posture (sekitar 90°)
        val kneeOk = kneeAdjusted > 145.0
        val hipOk  = hipAdjusted > 145.0

        Log.d(
            "VALIDATOR",
            "knee=${"%.1f".format(knee)} ok=$kneeOk | hip=${"%.1f".format(hip)} ok=$hipOk "
        )
        return kneeOk && hipOk
    }

    // ── Sit-up: posisi berbaring (WAITING_START) ─────────────
    fun isSitUpPosture(): Boolean {
        if (!isBodyVisible()) return false

        val spine = angleSpine()
        val knee  = angleKnee()
        // ✅ JANGAN pakai kneeOffset — offset dari kalibrasi berdiri
        //    tidak relevan untuk posisi berbaring
        // Dari log: knee raw ~47–65°, spine raw ~165–178°
        val spineOk = spine in 155.0..195.0
        val kneeOk  = knee in 40.0..90.0   // sesuai data log posisi berbaring

        Log.d("VALIDATOR", "isSitUpPosture: spine=${"%.1f".format(spine)} ok=$spineOk | knee=${"%.1f".format(knee)} ok=$kneeOk → ${spineOk && kneeOk}")
        return spineOk && kneeOk
    }
}