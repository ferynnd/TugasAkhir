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
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {

        if (poseResult == null) return@Canvas

        val landmarks = poseResult.landmarks().firstOrNull() ?: return@Canvas
        val width = size.width
        val height = size.height

        for (point in landmarks) {
            val x = point.x() * width
            val y = point.y() * height

            drawCircle(
                color = Color.Yellow,
                radius = 6f,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }

        drawSkeleton(landmarks, width, height)
    }
}

//val POSE_CONNECTIONS = listOf(
//    // TORSO
//    11 to 12,   // Kedua bahu
//    23 to 24,   // Kedua pinggul
//    11 to 23,   // Bahu kiri → pinggul kiri
//    12 to 24,   // Bahu kanan → pinggul kanan
//
//    // LENGAN KIRI
//    11 to 13,   // Bahu kiri → Siku kiri
//    13 to 15,   // Siku kiri → Pergelangan kiri
//    15 to 19,   // Pergelangan kiri → Sendi jari telunjuk (MCP)
//    19 to 17,   // MCP → PIP
//    17 to 21,   // PIP → Ujung jari (TIP)
//
//    // LENGAN KANAN
//    12 to 14,   // Bahu kanan → Siku kanan
//    14 to 16,   // Siku kanan → Pergelangan kanan
//    16 to 20,   // Pergelangan kanan → MCP telunjuk
//    20 to 18,   // MCP → PIP
//    18 to 22,   // PIP → TIP
//
//    // KAKI KIRI
//    23 to 25,   // Pinggul kiri → Lutut kiri
//    25 to 27,   // Lutut kiri → Pergelangan kaki kiri
//    27 to 31,   // Pergelangan kaki kiri → Telapak (Foot Index)
//    27 to 29,   // Pergelangan kaki kiri → Tumit
//
//    // KAKI KANAN
//    24 to 26,   // Pinggul kanan → Lutut kanan
//    26 to 28,   // Lutut kanan → Pergelangan kaki kanan
//    28 to 32,   // Pergelangan kaki kanan → Telapak (Foot Index)
//    28 to 30    // Pergelangan kaki kanan → Tumit
//)

// Definisikan grup koneksi dengan warnanya masing-masing
val TORSO_CONNECTIONS = listOf(11 to 12, 23 to 24, 11 to 23, 12 to 24)
val LEFT_ARM_CONNECTIONS = listOf(11 to 13, 13 to 15, 15 to 19, 19 to 17, 17 to 21)
val RIGHT_ARM_CONNECTIONS = listOf(12 to 14, 14 to 16, 16 to 20, 20 to 18, 18 to 22)
val LEFT_LEG_CONNECTIONS = listOf(23 to 25, 25 to 27, 27 to 31, 27 to 29)
val RIGHT_LEG_CONNECTIONS = listOf(24 to 26, 26 to 28, 28 to 32, 28 to 30)

private fun DrawScope.drawSkeleton(
    landmarks: List<NormalizedLandmark>,
    width: Float,
    height: Float
) {
    drawGroup(landmarks, TORSO_CONNECTIONS, width, height, Color.White)

    drawGroup(landmarks, LEFT_ARM_CONNECTIONS, width, height, Color.Yellow)
    drawGroup(landmarks, RIGHT_ARM_CONNECTIONS, width, height, Color.Yellow)

    drawGroup(landmarks, LEFT_LEG_CONNECTIONS, width, height, Color(0xFF22C55E)) // Hijau
    drawGroup(landmarks, RIGHT_LEG_CONNECTIONS, width, height, Color(0xFF22C55E))
}

private fun DrawScope.drawGroup(
    landmarks: List<NormalizedLandmark>,
    connections: List<Pair<Int, Int>>,
    width: Float,
    height: Float,
    color: Color
) {
    connections.forEach { (start, end) ->
        if (start < landmarks.size && end < landmarks.size) {
            val p1 = landmarks[start]
            val p2 = landmarks[end]

            drawLine(
                color = color,
                start = Offset(p1.x() * width, p1.y() * height),
                end = Offset(p2.x() * width, p2.y() * height),
                strokeWidth = 6f // Sedikit lebih tebal agar jelas
            )
        }
    }
}
