package dev.ferynnd.tugasakhir.ui.layouts

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.CameraActivity
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.components.BackgroundImageWithOverlay
import dev.ferynnd.tugasakhir.ui.components.OutlineButton
import dev.ferynnd.tugasakhir.ui.components.PrimaryButton
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.Secondary
import dev.ferynnd.tugasakhir.ui.theme.SpaceGrotesk

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize().statusBarsPadding().navigationBarsPadding()
    ) {
        BackgroundImageWithOverlay(
            modifier = Modifier.
                    background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            StatusChip(text = "MOTION DETECTED")

            Spacer(modifier = Modifier.height(32.dp))

            TitleSection()

            Spacer(modifier = Modifier.height(40.dp))

            PrimaryButton(
                text = "MULAI LATIHAN",
                icon = ImageVector.vectorResource(id = R.drawable.play),
                onClick = {
                    context.startActivity(Intent(context, CameraActivity::class.java))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlineButton(
                text = "LIST GERAKAN",
                icon = ImageVector.vectorResource(id = R.drawable.jump),
                onClick = {
                    navController.navigate("trainingList")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlineButton(
                text = "PROFILE",
                icon = ImageVector.vectorResource(id = R.drawable.profile),
                onClick = {}
            )
        }
    }
}



@Composable
fun StatusChip(text: String) {
    Row(
        modifier = Modifier
            .border(
                1.5.dp,
                Secondary,
                shape = RoundedCornerShape(50)
            )
            .background(
                Secondary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 20.dp, vertical = 8.dp)
            ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(Primary, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.White, fontSize = 12.sp)
    }
}

@Composable
fun TitleSection() {
    Column {
        Text(
            text = "Selamat",
            color = Color.White,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 52.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        )
        Text(
            text = "Datang Di",
            color = Color.White,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 52.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        )
        Text(
            text = "Training App",
            color = Primary,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 52.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Latihan dengan panduan kamera AI",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

