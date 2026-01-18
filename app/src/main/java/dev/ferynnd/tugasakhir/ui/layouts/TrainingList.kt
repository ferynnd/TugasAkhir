package dev.ferynnd.tugasakhir.ui.layouts


import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.components.ExerciseCard
import dev.ferynnd.tugasakhir.ui.theme.*

@Composable
fun TrainingList(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        containerColor = Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    Text(
                        text = "Choose Your",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextMain
                    )
                    Text(
                        text = "Exercise",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Primary
                    )
                }
            }

            item { ExerciseCard(
                title = "Squat",
                muscle = "Legs & Glutes",
                kcal = "12 kcal/min",
                imageRes = R.drawable.bgta,
                onDetail = {
                    Toast.makeText(context, "Coming Soon..", Toast.LENGTH_SHORT).show()
                },
                onStart = {
                    Toast.makeText(context, "Coming Soon..", Toast.LENGTH_SHORT).show()
                }
            ) }

            item { ExerciseCard(
                title = "Push-up",
                muscle = "Chest & Arms",
                kcal = "15 kcal/min",
                imageRes = R.drawable.bgta,
                 onDetail = {
                    Toast.makeText(context, "Coming Soon..", Toast.LENGTH_SHORT).show()
                },
                onStart = {
                    Toast.makeText(context, "Coming Soon..", Toast.LENGTH_SHORT).show()
                }
            ) }

            item { Spacer(modifier = Modifier.height(100.dp)) } // Space for bottom nav
        }
    }
}

