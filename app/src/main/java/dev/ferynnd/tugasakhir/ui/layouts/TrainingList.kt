package dev.ferynnd.tugasakhir.ui.layouts


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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.theme.*

@Composable
fun TrainingList(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

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

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Input),
                    placeholder = { Text("Search movements...", color = TextSub) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSub) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Primary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }


            item { ExerciseCard(
                title = "Squat",
                muscle = "Legs & Glutes",
                kcal = "12 kcal/min",
                level = "MEDIUM",
                imageRes = R.drawable.bgta, // Ganti dengan gambar squat
                onClick = { navController.navigate("trainingDetail") }
            ) }

            item { ExerciseCard(
                title = "Push-up",
                muscle = "Chest & Arms",
                kcal = "15 kcal/min",
                level = "HARD",
                imageRes = R.drawable.bgta, // Ganti dengan gambar pushup
                onClick = { navController.navigate("trainingDetail") }
            ) }

            item { Spacer(modifier = Modifier.height(100.dp)) } // Space for bottom nav
        }
    }
}

@Composable
fun ExerciseCard(
    title: String,
    muscle: String,
    kcal: String,
    level: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.8.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                // Level Tag
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    color = if (level == "HARD") Primary else Color(0xFF64949C),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = level,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMain)
                    Text(text = muscle, fontSize = 14.sp, color = TextSub)
                }

                // Kcal Info Badge
                Surface(
                    color = Primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icfire), // Pastikan icon ini ada
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = kcal, color = Primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(id = R.drawable.icvideo), contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Start Analysis", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}