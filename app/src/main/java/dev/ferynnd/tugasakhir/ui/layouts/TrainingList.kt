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
import dev.ferynnd.tugasakhir.data.model.dummyExercises
import dev.ferynnd.tugasakhir.ui.components.ExerciseCard
import dev.ferynnd.tugasakhir.ui.theme.*

@Composable
fun TrainingList(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Text(
                    text = "Pilih Latihan",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextMain
                )
                Text(
                    text = "Untuk Kamu",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

           LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
           ) {
                items(dummyExercises) { dummyExercise ->
                    ExerciseCard(
                        title = dummyExercise.name,
                        muscle = dummyExercise.duration + " Min ",
                        kcal = dummyExercise.cal,
                        imageRes = dummyExercise.icon,
                        onDetail = {
                            navController.navigate("trainingDetail/${dummyExercise.id}")
                        },
                        onStart = {
                            navController.navigate("cameraScan/${dummyExercise.code}")
                        }
                    )
                }
           }

        }
    }
}

