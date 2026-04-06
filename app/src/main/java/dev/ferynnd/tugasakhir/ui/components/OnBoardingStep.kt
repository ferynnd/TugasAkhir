package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Primary

data class OnboardingStep(
    val stepLabel: String,
    val title: String,
    val body: String,
    val tip: String
)

fun getOnboardingSteps(exerciseCode: ExerciseCode): List<OnboardingStep> {
    // Kamu bisa sesuaikan teks per jenis latihan
    val exerciseName = when (exerciseCode) {
        ExerciseCode.PUSH_UP -> "push up"
        ExerciseCode.SQUAT   -> "squat"
        ExerciseCode.SIT_UP  -> "sit up"
    }

    return listOf(
        OnboardingStep(
            stepLabel = "Persiapan",
            title = "Tampilkan seluruh tubuh dulu",
            body = "Sebelum mulai, aplikasi akan meminta kamu berdiri " +
                    "sehingga seluruh tubuh — dari kepala sampai kaki — " +
                    "terlihat oleh kamera.",
            tip = "Letakkan HP di posisi stabil, sekitar 2–3 meter " +
                    "di depanmu. Pastikan pencahayaan cukup terang."
        ),
        OnboardingStep(
            stepLabel = "Kalibrasi",
            title = "Aplikasi mendeteksi posisi tubuhmu",
            body = "Setelah kamu tekan Mulai, kamera akan memindai posisimu. " +
                    "Lingkaran progress menunjukkan seberapa jauh deteksi berjalan. " +
                    "Tetap diam dan berdiri tegak.",
            tip = "Jangan bergerak saat lingkaran belum penuh. Kalau angka " +
                    "tidak naik, coba mundur agar seluruh badan terlihat."
        ),
        OnboardingStep(
            stepLabel = "Latihan",
            title = "Mulai $exerciseName — rep dihitung otomatis",
            body = "Setelah kalibrasi selesai, langsung lakukan gerakan. " +
                    "Pastikan tubuh terlihat sepenuhnya di kamera dari sisi samping (side view), " +
                    "agar sistem dapat mendeteksi gerakan dengan akurat. " +
                    "Setiap repetisi yang dilakukan dengan benar akan otomatis menambah jumlah REPS.",
            tip = "Gunakan posisi samping terhadap kamera dan jaga seluruh tubuh tetap terlihat. " +
                    "Lakukan gerakan secara penuh dan terkontrol agar setiap repetisi dapat terhitung."
        ),
        OnboardingStep(
            stepLabel = "Koreksi Gerakan",
            title = "Peringatan postur muncul otomatis",
            body = "Saat posisi atau gerakan kurang tepat, aplikasi akan " +
                    "menampilkan peringatan KOREKSI GERAKAN secara otomatis berdasarkan analisis kamera secara real-time. " +
                    "Ikuti arahan yang muncul untuk memperbaiki postur.",
            tip = "Jika peringatan muncul, cukup sesuaikan posisi tubuhmu (misalnya luruskan badan atau perbaiki sudut siku), " +
                    "lalu lanjutkan latihan seperti biasa."
        )
    )
}

@Composable
fun OnboardingOverlay(
    exerciseCode: ExerciseCode,
    onFinish: () -> Unit
) {
    val steps = remember { getOnboardingSteps(exerciseCode) }
    var currentStep by remember { mutableStateOf(0) }
    val step = steps[currentStep]
    val isLast = currentStep == steps.lastIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .background(Card, RoundedCornerShape(16.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Progress dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    steps.forEachIndexed { index, _ ->
                        val color = when {
                            index < currentStep  -> Primary.copy(alpha = 0.5f)
                            index == currentStep -> Primary
                            else                 -> Color.White.copy(alpha = 0.2f)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .background(color, RoundedCornerShape(2.dp))
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                // Tombol X
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            Color.White.copy(alpha = 0.08f),
                            RoundedCornerShape(50)
                        )
                        .clickable { onFinish() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            // Step badge
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.White.copy(alpha = 0.08f),
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Langkah ${currentStep + 1} dari ${steps.size}  ·  ${step.stepLabel}",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            // Title
            Text(
                text = step.title,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Body
            Text(
                text = step.body,
                style = TextStyle(
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // Tip box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.06f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("💡", fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = step.tip,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 18.sp
                    )
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tombol Kembali
                OutlinedButton(
                    onClick = { if (currentStep > 0) currentStep-- },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    enabled = currentStep > 0,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Kembali", fontSize = 13.sp)
                }

                // Tombol Lanjut / Mulai
                Button(
                    onClick = {
                        if (isLast) onFinish()
                        else currentStep++
                    },
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    )
                ) {
                    Text(
                        text = if (isLast) "Siap, Mulai! ✓" else "Lanjut",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}