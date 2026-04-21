package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colError
import dev.ferynnd.tugasakhir.ui.theme.colFire
import dev.ferynnd.tugasakhir.ui.theme.colTime
import dev.ferynnd.tugasakhir.ui.theme.colWarning
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


@Composable
fun RepsBadge(count: Int, modifier: Modifier) {
    Box(
        modifier = modifier
            .size(100.dp, 80.dp)
            .background(Color.White, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("REPS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF1C1C1E))
            Text(count.toString(), fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }
    }
}

@Composable
fun BottomInfoPanel(onEndSessionClick: () -> Unit,
                    feedback: String, elapsedTime: Int,
                    modifier: Modifier,
                    exerciseName: String
) {

    val minutes = elapsedTime / 60
    val seconds = elapsedTime % 60

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                .border(1.5.dp, Primary.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Ikon bulat hijau
                Box(modifier = Modifier.size(40.dp).background(Primary.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Info, null, tint = Primary)
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text("KOREKSI GERAKAN", color = Primary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text(feedback, color = Color.White, fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onEndSessionClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.5.dp, colError.copy(alpha = 0.8f)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = colError.copy(alpha = 0.6f))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(12.dp).background(White, RectangleShape))
                Spacer(Modifier.width(10.dp))
                Text("AKHIRI LATIHAN", color = White, fontWeight = FontWeight.Black)
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                color = Card.copy(0.7f),
                border = BorderStroke(1.5.dp, Card.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.icbodyscan), null, tint = colFire, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("LIVE ${exerciseName}" , fontSize = 12.sp, fontWeight = FontWeight.Bold, color = White)
                }
            }

            Surface(
                color = Card.copy(0.7f),
                border = BorderStroke(1.5.dp, Card.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.icoclock), null, tint = colTime, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text( String.format("%02d:%02d", minutes, seconds) , fontSize = 12.sp, fontWeight = FontWeight.Bold, color = White)
                }
            }
        }
    }
}

@Composable
fun CountdownOverlay(count: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black.copy(alpha = 0.88f)), // Latar belakang gelap pekat
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text(
                text = if (count > 0) count.toString() else "MULAI",
                style = TextStyle(
                    fontSize = 100.sp,
                    fontWeight = FontWeight.Black,
                    color = Primary,
                    shadow = Shadow(
                        color = Primary.copy(alpha = 0.8f),
                        blurRadius = 50f
                    )
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "BERSIAP DALAM...",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 4.sp
                )
            )
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
