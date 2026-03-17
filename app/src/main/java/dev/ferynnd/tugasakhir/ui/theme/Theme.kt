package dev.ferynnd.tugasakhir.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
//
//private val DarkColorScheme = darkColorScheme(
//    primary = Primary,
//    onPrimary = TextMain,
//    secondary = Secondary,
//    onSecondary = White,
//    tertiary = Card,
//    onTertiary = White,
//    background = Background,
//    onBackground = White,
//    surface = Card,
//    onSurface = White,
//    outline = Border,
//    surfaceVariant = Input,
//    onSurfaceVariant = White
//)


private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = TextSub,
    secondary = colHeart,
    onSecondary = White,
    tertiary = Card,
    onTertiary = White,
    background = Background,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    outline = TextSub,
    surfaceVariant = Input,
    onSurfaceVariant = Black
)


@Composable
fun TugasAkhirTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}