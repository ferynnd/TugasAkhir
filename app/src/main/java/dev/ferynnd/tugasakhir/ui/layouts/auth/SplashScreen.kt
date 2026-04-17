package dev.ferynnd.tugasakhir.ui.layouts.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.ui.theme.Primary // Warna Red Sporty
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.TextSub

@Composable
fun SplashScreenNavigation(onNavigationComplete: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000) // Tampilkan selama 3 detik
        onNavigationComplete()
    }
    SplashScreen()
}
@Preview
@Composable
fun SplashScreen() {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f, // Sesuai gambar 84%
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background) // Background sedikit keabu-abuan halus
            .drawBehind {
                val dotColor = Card.copy(alpha = 0.3f)
                val spacing = 25.dp.toPx()
                for (x in 0..size.width.toInt() step spacing.toInt()) {
                    for (y in 0..size.height.toInt() step spacing.toInt()) {
                        drawCircle(dotColor, radius = 1f, center = Offset(x.toFloat(), y.toFloat()))
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                painter = painterResource(id = R.drawable.fitlenlogo),
                contentDescription = "Logo",
                tint = Primary,
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "E V A L U A T I O N   E N G I N E",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSub,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))

            Column(modifier = Modifier.width(300.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "LOADING SUMBER DAYA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSub
                    )
                    Text(
                        text = "${(animatedProgress.value * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Primary,
                    trackColor = Color(0xFFE9ECEF),
                    strokeCap = StrokeCap.Round
                )

                Text(
                    text = "Mempersiapkan AI Pose Detector...",
                    fontSize = 11.sp,
                    color = TextSub,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 12.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .background(Card, RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "V 1.0.0 • RESEARCH EDITION",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextSub
            )
        }
    }
}