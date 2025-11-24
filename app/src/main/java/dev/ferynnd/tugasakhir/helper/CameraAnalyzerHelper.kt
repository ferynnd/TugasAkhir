package dev.ferynnd.tugasakhir.helper

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.google.mediapipe.framework.image.BitmapImageBuilder

class CameraAnalyzerHelper(
    private val poseHelper: PoseLandmarkerHelper,
    private val isFrontCamera: Boolean = true
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {

        val bitmap = image.toBitmap() ?: run {
            image.close()
            return
        }

        // rotate sesuai orientasi kamera
        val matrix = Matrix().apply {
            postRotate(image.imageInfo.rotationDegrees.toFloat())

            if (isFrontCamera) {
                postScale(-1f, 1f, bitmap.width.toFloat(), bitmap.height.toFloat())
            }
        }

        val rotated = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )

        val mpImage = BitmapImageBuilder(rotated).build()
        val timestamp = System.currentTimeMillis()

        poseHelper.detectAsync(mpImage, timestamp)

        image.close()
    }
}

fun ImageProxy.toBitmap(): Bitmap? {
    val plane = planes.firstOrNull() ?: return null
    val buffer = plane.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
