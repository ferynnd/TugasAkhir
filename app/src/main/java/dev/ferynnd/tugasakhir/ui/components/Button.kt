package dev.ferynnd.tugasakhir.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Primary


@Composable
fun BackButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary
        ),
        shape = RoundedCornerShape(100),
        contentPadding = PaddingValues(3.dp),
        modifier = modifier.size(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Back",
            tint = Black,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SmallFab(
    icon: Int,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .background(
                color = Primary.copy(alpha = 0.9f),
                shape = RoundedCornerShape(30)
            )
            .clickable(onClick = onClick)
    ) {
        CustomIcon(
            iconRes = icon,
            contentDescription = null,
            tint = Black,
            modifier = Modifier.size(22.dp)
        )
    }
}

