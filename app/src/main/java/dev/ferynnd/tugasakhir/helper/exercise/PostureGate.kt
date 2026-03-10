package dev.ferynnd.tugasakhir.helper.exercise

import android.util.Log


/**
 * Gate yang hanya reset state jika postur tidak valid
 * selama [invalidFramesRequired] frame berturut-turut.
 *
 * Mencegah reset akibat 1-2 frame noise dari MediaPipe.
 */
class PostureGate(
    private val invalidFramesRequired: Int = 10  // ~0.5 detik pada 20fps
) {
    private var invalidFrameCount = 0

    /**
     * Panggil setiap frame.
     * @return true = postur dianggap tidak valid (boleh reset)
     *         false = masih dalam toleransi, jangan reset dulu
     */
    fun update(isValid: Boolean): Boolean {
        return if (isValid) {
            invalidFrameCount = 0  // reset counter kalau valid lagi
            false                  // tidak perlu reset state
        } else {
            invalidFrameCount++
            Log.d("POSTURE_GATE", "Invalid frame: $invalidFrameCount/$invalidFramesRequired")
            invalidFrameCount >= invalidFramesRequired  // reset hanya setelah N frame invalid
        }
    }

    fun reset() {
        Log.d("POSTURE_GATE", "Gate reset")
        invalidFrameCount = 0
    }
}