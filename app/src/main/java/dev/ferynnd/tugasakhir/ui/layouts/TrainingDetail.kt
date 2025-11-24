package dev.ferynnd.tugasakhir.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.components.BackButton
import dev.ferynnd.tugasakhir.ui.components.BackgroundImageWithOverlay
import dev.ferynnd.tugasakhir.ui.components.DetailCard
import dev.ferynnd.tugasakhir.ui.components.PrimaryButton
import dev.ferynnd.tugasakhir.ui.components.TrainingCard
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.Secondary


@Composable
fun TrainingDetail(navController: NavController) {

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize().statusBarsPadding().navigationBarsPadding()
    ) {

        BackgroundImageWithOverlay(
            modifier = Modifier.
                background(Color.Black.copy(alpha = 0.8f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .verticalScroll(scrollState)
            ,
            verticalArrangement = Arrangement.SpaceBetween // penting!!
        ) {

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.bgta),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )
                }


                    BackButton(
                        icon = Icons.Default.ArrowBack,
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 6.dp, end = 6.dp, top = 10.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Push Up",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    )

                    Spacer(Modifier.height(8.dp))

                    StatusBadge(text = "STRENGTH TRAINING")

                    Spacer(Modifier.height(20.dp))

                    DetailCard(
                        title = "PENJELASAN",
                        text = "Lorem Ipsum is simply dummy text..."
                    )

                    Spacer(Modifier.height(10.dp))

                    DetailCard(
                        title = "FUNGSI",
                        text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                    )

                    Spacer(Modifier.height(10.dp))

                    DetailCard(
                        title = "FUNGSI",
                        text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                    )

                }

            }

            PrimaryButton(
                text = "MULAI LATIHAN",
                icon = ImageVector.vectorResource(id = R.drawable.play),
                onClick = { },
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}


@Composable
fun StatusBadge(text: String) {
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
        Text(text, color = Color.White, fontSize = 12.sp)
    }
}
