package dev.ferynnd.tugasakhir.ui.layouts

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.components.BackButton
import dev.ferynnd.tugasakhir.ui.components.BackgroundImageWithOverlay
import dev.ferynnd.tugasakhir.ui.components.TrainingCard
import dev.ferynnd.tugasakhir.ui.theme.Primary

@Composable
fun TrainingList(navController: NavController) {

     val trainingItems = listOf(
        R.drawable.bgta,
        R.drawable.bgta,
        R.drawable.bgta,
        R.drawable.bgta,
        R.drawable.bgta,
        R.drawable.bgta
    )

    Box(
        modifier = Modifier
            .fillMaxSize().statusBarsPadding().navigationBarsPadding()
    ) {
        BackgroundImageWithOverlay(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            BackButton(
                icon = Icons.Default.ArrowBack,
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 25.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "List Latihan",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp),
            )
            GradientLine(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(top = 15.dp, start = 10.dp)
            )


            Spacer(modifier = Modifier.height(20.dp))

             LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(trainingItems.size) { index ->
                    TrainingCard(
                        imageRes = trainingItems[index],
                        title = "Push Up",
                        onClick = {
                            navController.navigate("trainingDetail")
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun GradientLine(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Primary,
        Color.Transparent
    )
) {
    Box(
        modifier = modifier
            .height(5.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(colors)
            )
    )
}

