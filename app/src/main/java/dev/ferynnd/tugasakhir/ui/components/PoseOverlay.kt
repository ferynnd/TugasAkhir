package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseOverlay(
    poseResult: PoseLandmarkerResult?,
    modifier: Modifier = Modifier,
    correct: Boolean
) {
    val skeletonColor = if (correct) Color(0xFF00E676) else Color(0xFFFF1744)
    val jointColor = if (correct) Color(0xFF69F0AE) else Color(0xFFFF6B6B)

    Canvas(modifier = modifier.fillMaxSize()) {

        if (poseResult == null) return@Canvas // Jangan gambar apa-apa

        val landmarks = poseResult.landmarks().firstOrNull() ?: return@Canvas
        // hasil landmark itu bukan pixel, tapi normalized coordinate
        val width = size.width
        val height = size.height

        for (point in landmarks) {
            val x = point.x() * width
            val y = point.y() * height
            // val diatas untuk mengubah koordinat 0–1 → pixel layar

            drawCircle(
                color = jointColor,
                radius = 6f,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }

        drawSkeleton(landmarks, width, height, skeletonColor)
    }
}

// Definisikan grup koneksi dengan warnanya masing-masing
val TORSO_CONNECTIONS = listOf(11 to 12, 23 to 24, 11 to 23, 12 to 24)
val LEFT_ARM_CONNECTIONS = listOf(11 to 13, 13 to 15, 15 to 19, 19 to 17, 17 to 21)
val RIGHT_ARM_CONNECTIONS = listOf(12 to 14, 14 to 16, 16 to 20, 20 to 18, 18 to 22)
val LEFT_LEG_CONNECTIONS = listOf(23 to 25, 25 to 27, 27 to 31, 27 to 29)
val RIGHT_LEG_CONNECTIONS = listOf(24 to 26, 26 to 28, 28 to 32, 28 to 30)

private fun DrawScope.drawSkeleton(
    landmarks: List<NormalizedLandmark>,
    width: Float,
    height: Float,
    color: Color
) {
    drawGroup(landmarks, TORSO_CONNECTIONS, width, height, color)

    drawGroup(landmarks, LEFT_ARM_CONNECTIONS, width, height, color)
    drawGroup(landmarks, RIGHT_ARM_CONNECTIONS, width, height, color)

    drawGroup(landmarks, LEFT_LEG_CONNECTIONS, width, height, color)
    drawGroup(landmarks, RIGHT_LEG_CONNECTIONS, width, height,color)
}

private fun DrawScope.drawGroup(
    landmarks: List<NormalizedLandmark>,
    connections: List<Pair<Int, Int>>,
    width: Float,
    height: Float,
    color: Color
) {
    connections.forEach { (start, end) ->
        // mengambil dua nilai dari Pair (start, end) dengan destructuring
        if (start < landmarks.size && end < landmarks.size) {
            val p1 = landmarks[start]
            val p2 = landmarks[end]

            drawLine(
                color = color,
                start = Offset(p1.x() * width, p1.y() * height), // mengubah ke pixel
                end = Offset(p2.x() * width, p2.y() * height),
                strokeWidth = 6f // Sedikit lebih tebal agar jelas
            )
        }
    }
}
