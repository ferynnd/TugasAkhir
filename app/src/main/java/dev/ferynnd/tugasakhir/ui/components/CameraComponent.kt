package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colWarning
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


@Composable
fun RepsBadge(count: Int, modifier: Modifier = Modifier)
{
    Column(
        modifier = modifier
            .size(64.dp)
            .clip(RectangleShape)
            .background(Brush.verticalGradient(listOf(Primary, Color(0xFF991B1B))), RoundedCornerShape(10.dp))
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "REPS",
            color = White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = count.toString(),
            color = Color(0xFFF3F10A), // Warna merah sesuai desain
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun BottomInfoPanel(
    modifier: Modifier = Modifier,
    onEndSessionClick: () -> Unit,
    feedback: String? = null,
    isCountdownActive: Boolean = false
) {

    val elapsedSeconds = rememberElapsedSeconds(isRunning = !isCountdownActive)

    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {

        // Judul dengan Icon Bulat Pink
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFEBEB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFFE12524)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Koreksi Gerakan",
                color = Color(0xFF1A1C1E),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = feedback.toString(),
            fontWeight = FontWeight.Medium,
            color = TextSub,
            fontSize = 18.sp,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        SessionRunningCard(
            elapsedTime = elapsedSeconds,
            onEndSessionClick = onEndSessionClick
        )
    }
}

@Composable
fun CountdownOverlay(count: Int)
{

    val animatedScale by animateFloatAsState(
        targetValue = 1.2f,
        animationSpec = tween(600),
        label = ""
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (count > 0) count.toString() else "Mulai",
            fontSize = 90.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = animatedScale,
                    scaleY = animatedScale,
                    alpha = animatedAlpha
                )
        )
    }
}

@Composable
fun SessionRunningCard(
    elapsedTime: Int,
    onEndSessionClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = TextSub.copy(alpha = 0.15f) // seperti textSub.copy(0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            val minutes = elapsedTime / 60
            val seconds = elapsedTime % 60


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TIME ELAPSED",
                        fontSize = 12.sp,
                        color = Black.copy(alpha = 0.8f)
                    )

                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSub
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onEndSessionClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        CustomIcon(
                            iconRes = R.drawable.cancel,
                            contentDescription = "berhenti",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "AKHIRI LATIHAN",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun rememberElapsedSeconds(isRunning: Boolean): Int {
    var startTime by remember { mutableStateOf<Long?>(null) }
    var currentTime by remember { mutableStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            if (startTime == null) {
                startTime = System.currentTimeMillis()
            }
            while (isActive) {
                currentTime = System.currentTimeMillis()
                delay(1000)
            }
        }
    }

    return startTime?.let {
        ((currentTime - it) / 1000).toInt()
    } ?: 0
}

@Composable
fun QuitDialog(
    onDismissRequest: () -> Unit,
    onConfirmQuit: () -> Unit
) {
    LottieDialog(
        lottieRes = R.raw.warning,
        title = "Keluar",
        message = "Apakah Anda yakin ingin keluar, ini tidak akan menyimpan progress latihan anda?",
        confirmText = "Ya",
        dismissText = "Tidak",
        colorBg = colWarning.copy(alpha = 0.15f),
        onConfirm = {
            onConfirmQuit()
        },
        onDismiss = {
            onDismissRequest()
        }
    )
}

@Composable
fun FinishDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    LottieDialog(
        lottieRes = R.raw.warning,
        title = "Selesai?",
        message = "Apakah anda yakin ingin menyelesaikan latihan?",
        confirmText = "Ya",
        dismissText = "Tidak",
        colorBg = colWarning.copy(alpha = 0.15f),
        onConfirm = {
            onConfirm()
        },
        onDismiss = {
            onDismissRequest()
        }
    )
}
