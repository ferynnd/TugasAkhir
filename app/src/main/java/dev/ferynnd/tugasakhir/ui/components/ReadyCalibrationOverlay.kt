package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Primary
import kotlin.io.path.Path
import kotlin.io.path.moveTo

@Composable
fun ReadyCalibrationOverlay(
    exerciseCode: ExerciseCode,
    instruction: String,
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(Card, RoundedCornerShape(12.dp)) // Dialog Gelap
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ikon Ilustrasi Minimalis (Sesuai Gambar 2)
//            Box(
//                modifier = Modifier
//                    .size(120.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                CustomIcon(
//                    iconRes = R.drawable.jump,
//                    contentDescription = "Person Icon",
//                    tint = Primary,
//                    modifier = Modifier.size(100.dp)
//                )
//            }

            Text(
                text = "Tampilkan seluruh tubuh, diam sebentar...",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (instruction.isNotEmpty()) instruction else "-",
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Mulai Kalibrasi", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun CalibrationProgressOverlay(progress: Float, instruction: String, isBodyDetected: Boolean) {
    Box(modifier = Modifier.fillMaxSize().background(Black.copy(alpha = 0.8f))) {
        // Bingkai Kamera (Siku di pojok)
        CameraFrameGuides(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Progress Melingkar di Tengah (Sesuai Gambar 3)
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(180.dp),
                    color = Primary,
                    strokeWidth = 8.dp,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(  painter = painterResource(id = R.drawable.icbodyscan), null, tint = Primary, modifier = Modifier.size(30.dp))
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black, color = Primary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Glassmorphism Label "Mendeteksi Posisi"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Primary, CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text("Mendeteksi Posisi...", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun CalibrationDoneOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.iccheck),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Kalibrasi Selesai!",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bersiap...",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 15.sp
            )
        }
    }
}

enum class ScreenPhase {
    WAITING_CALIBRATION,  // Overlay awal "Siap kalibrasi?"
    CALIBRATING,          // Progress bar kalibrasi
    CALIBRATION_DONE,     // Selesai, langsung trigger countdown
    COUNTDOWN,            // 5,4,3,2,1
    EXERCISE              // Latihan berjalan
}

@Composable
fun CameraFrameGuides(
    modifier: Modifier = Modifier,
    color: Color = Primary, // Gunakan warna Lime Green Anda
    strokeWidth: Float = 8f,
    cornerLength: Float = 60f
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val padding = 40f // Jarak garis dari tepi layar

        // 1. Pojok Kiri Atas
        drawPath(
            path = Path().apply {
                moveTo(padding, padding + cornerLength)
                lineTo(padding, padding)
                lineTo(padding + cornerLength, padding)
            } as Path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 2. Pojok Kanan Atas
        drawPath(
            path = Path().apply {
                moveTo(width - padding - cornerLength, padding)
                lineTo(width - padding, padding)
                lineTo(width - padding, padding + cornerLength)
            },
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 3. Pojok Kiri Bawah
        drawPath(
            path = Path().apply {
                moveTo(padding, height - padding - cornerLength)
                lineTo(padding, height - padding)
                lineTo(padding + cornerLength, height - padding)
            },
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 4. Pojok Kanan Bawah
        drawPath(
            path = Path().apply {
                moveTo(width - padding - cornerLength, height - padding)
                lineTo(width - padding, height - padding)
                lineTo(width - padding, height - padding - cornerLength)
            },
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}