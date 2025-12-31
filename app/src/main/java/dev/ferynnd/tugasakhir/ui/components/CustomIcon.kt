package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.material3.Icon
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomIcon(
    @DrawableRes iconRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Black,
    backgroundColor: Color? = null,
    cornerRadius: Dp = 0.dp,
    padding: Dp = 0.dp
) {
    // 1. Tentukan modifier dasar
    var boxModifier = modifier

    // 2. Tambahkan background dan clip jika backgroundColor tidak null
    if (backgroundColor != null) {
        boxModifier = boxModifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .padding(padding)
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}