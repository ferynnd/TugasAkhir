package dev.ferynnd.tugasakhir.helper

import android.content.Context
import android.util.Log
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseLandmarkerHelper(
    private val context: Context,
    private val listener: Listener,
    private val runningMode: RunningMode = RunningMode.LIVE_STREAM
) {

    private var landmarker: PoseLandmarker? = null

    interface Listener {
        fun onPoseResult(result: PoseLandmarkerResult, inferenceTime: Long)
        fun onError(message: String)
    }

    init {
        setupLandmarker()
    }

    private fun setupLandmarker() {
        // Untuk LIVE_STREAM, listener wajib ada — sesuai contoh resmi
        if (runningMode == RunningMode.LIVE_STREAM && listener == null) {
            throw IllegalStateException(
                "Listener must not be null when running LIVE_STREAM mode."
            )
        }

        try {
            // Base model options
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("pose_landmarker_full.task")
                .build()

            // Build PoseLandmarker options
            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setRunningMode(runningMode)
                .setMinPoseDetectionConfidence(0.5f)
                .setMinTrackingConfidence(0.5f)
                .setMinPosePresenceConfidence(0.5f)
                .apply {
                    if (runningMode == RunningMode.LIVE_STREAM) {
                        setResultListener(::onLiveStreamResult)
                        setErrorListener(::onLiveStreamError)
                    }
                }
                .build()

            landmarker = PoseLandmarker.createFromOptions(context, options)

        } catch (e: Exception) {
            listener.onError("Failed to initialize PoseLandmarker: ${e.message}")
            Log.e("PoseLandmarkerHelper", e.toString())
        }
    }

    // Listener resmi: (PoseLandmarkerResult, MPImage)
    private fun onLiveStreamResult(
        result: PoseLandmarkerResult,
        input: MPImage
    ) {
        val inferenceTime = System.currentTimeMillis() - result.timestampMs()
        listener.onPoseResult(result, inferenceTime)
    }

    private fun onLiveStreamError(error: RuntimeException) {
        listener.onError(error.message ?: "Unknown error occurred")
    }

    fun detectAsync(mpImage: MPImage, timestamp: Long) {
        landmarker?.detectAsync(mpImage, timestamp)
    }

    fun close() {
        landmarker?.close()
        landmarker = null
    }
}
