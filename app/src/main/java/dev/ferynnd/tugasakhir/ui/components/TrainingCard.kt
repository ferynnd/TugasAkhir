package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.ui.theme.Border
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Geist


@Composable
fun TrainingCard(
    imageRes: Int,
    title: String,
    onClick: () -> Unit
) {
    var isSelected by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Card else Color.Transparent,
        animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, Border, RoundedCornerShape(16.dp))
            .background(Card.copy(alpha = 0.8f))
            .clickable {
                isSelected = !isSelected
                onClick()
            }
            .padding(10.dp)
    ) {

        Column(
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = Geist,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
            )
        }
    }
}
