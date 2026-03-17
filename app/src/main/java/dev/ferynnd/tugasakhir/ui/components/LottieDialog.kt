package dev.ferynnd.tugasakhir.ui.components

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Geist
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextMain
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.Typography
import dev.ferynnd.tugasakhir.ui.theme.White
import kotlinx.coroutines.delay


@Composable
fun LottieDialog(
    lottieRes: Int,
    title: String,
    message: String,
    confirmText: String = "OK",
    colorBg: Color = Card,
    dismissText: String? = null,
    autoDismiss: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    timeDelay : Long = 3000
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Card.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {

            AnimatedVisibility(
                visible = true,
                enter = scaleIn(initialScale = 0.9f) + fadeIn(),
                exit = scaleOut(targetScale = 0.95f) + fadeOut()
            ) {

                Card(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Card
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 12.dp
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = 24.dp,
                                vertical = 28.dp
                            )
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                                .height(140.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorBg
                            ),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                LottieIcon(
                                    resId = lottieRes,
                                    size = 120.dp,
                                )
                            }
                        }

                        Text(
                            style = Typography.titleLarge,
                            text = title,
                            fontFamily = Geist,
                            color = White,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = message,
                            fontFamily = Geist,
                            style = Typography.bodyMedium,
                            color = TextSub,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )

                        Spacer(Modifier.height(28.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            dismissText?.let {
                                OutlinedButton(
                                    onClick = onDismiss,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(50.dp)
                                ) {
                                    Text(it, color = White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                }

                                Spacer(Modifier.width(12.dp))
                            }

                            Button(
                                onClick = onConfirm,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(50.dp)
                            ) {
                                Text(confirmText, color = Black, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }

    // 🔹 AUTO DISMISS
    if (autoDismiss) {
        LaunchedEffect(Unit) {
            delay(timeDelay)
            onDismiss()
        }
    }
}


@Composable
fun LottieIcon(
    @RawRes resId: Int,
    size: Dp = 120.dp,
    iterations: Int = 1
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(resId)
    )

    LottieAnimation(
        composition = composition,
        iterations = iterations,
        modifier = Modifier.size(size).background(Color.Transparent)
    )
}


data class DialogState(
    val lottieRes: Int,
    val colorBg: Color = Card,
    val title: String,
    val message: String,
    val autoDismiss: Boolean = false
)
