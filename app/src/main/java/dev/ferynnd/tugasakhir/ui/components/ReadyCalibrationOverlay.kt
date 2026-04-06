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
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Black.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .background(Card, RoundedCornerShape(16.dp))
                    .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ikon Ilustrasi Minimalis (Sesuai Gambar 2)
            Box(
                modifier = Modifier
                    .background(Primary.copy(alpha = 0.1f), RoundedCornerShape(50))
                    .size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(
                    iconRes = R.drawable.jump,
                    contentDescription = "Person Icon",
                    tint = Primary,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Tampilkan seluruh tubuh, diam sebentar...",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Mulai Sekarang", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun CalibrationProgressOverlay(
    progress: Float,
    message: String = "Pastikan seluruh tubuh terlihat",
    isStable: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black.copy(alpha = 0.88f))
    ) {

        // Frame Guide (tetap dipakai)
//        CameraFrameGuides(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 🔥 Progress Circle
            Box(contentAlignment = Alignment.Center) {

                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(180.dp),
                    color = Primary,
                    strokeWidth = 10.dp,
                    trackColor = Color.White.copy(alpha = 0.15f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Kalibrasi",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔥 Instruction utama (INI PENTING)
            Text(
                text = message,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 Status Indicator (dinamis)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.08f))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            if (isStable) Primary else Color.Yellow,
                            CircleShape
                        )
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = if (isStable)
                        "Posisi stabil, lanjutkan..."
                    else
                        "Menyesuaikan posisi...",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun CalibrationDoneOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.88f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier =  Modifier.padding(24.dp)) {
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
    ONBOARDING,           // Overlay awal "Tampilkan seluruh tubuh dulu"
    WAITING_CALIBRATION,  // Overlay awal "Siap kalibrasi?"
    CALIBRATING,          // Progress bar kalibrasi
    CALIBRATION_DONE,     // Selesai, langsung trigger countdown
    COUNTDOWN,            // 5,4,3,2,1
    EXERCISE              // Latihan berjalan
}

@Composable
fun CameraFrameGuides(
    modifier: Modifier = Modifier,
    color: Color = Primary,
    strokeWidth: Float = 8f,
    cornerLength: Float = 60f
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val padding = 40f

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


