package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Secondary


@Composable
fun BackgroundImageWithOverlay(
     modifier: Modifier = Modifier,
) {
    Box(Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.bgta),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay Gelap
        Box(
            modifier = modifier
                .fillMaxSize()
        )

        // Gradient Ungu Gelap
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Secondary.copy(alpha = 0.65f),
                            Color(0xFF1F167B).copy(alpha = 0.45f),
                            Black.copy(alpha = 0.95f)
                        )
                    )
                )
        )
    }
}
