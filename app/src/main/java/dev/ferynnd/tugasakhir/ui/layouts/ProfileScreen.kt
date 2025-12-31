package dev.ferynnd.tugasakhir.ui.layouts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun ProfileScreen(navController: NavController) {
    Scaffold(
        containerColor = Background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Profile", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextMain)
                IconButton(
                    onClick = { navController.navigate("bmiProfile") },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Card, CircleShape)
                        .border(1.dp, Border, CircleShape)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header Profile
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.profile), // Ganti dengan drawable Anda
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFF22C55E), CircleShape)
                                .border(4.dp, White, CircleShape)
                                .align(Alignment.BottomEnd)
                                .offset(x = (-8).dp, y = (-8).dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Alex Johnson", fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextMain)
//                    Text("Subject ID: #88392", fontSize = 14.sp, color = TextSub)
//                    Text("Member Since 2023", fontSize = 12.sp, color = TextSub)
                }
            }

            // Biometrics Section
            item {
                Text("Biometrics", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextMain)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BiometricCard(modifier = Modifier.weight(1f), value = "175", unit = "cm", label = "HEIGHT")
                    BiometricCard(modifier = Modifier.weight(1f), value = "70", unit = "kg", label = "WEIGHT")
                    BiometricCard(modifier = Modifier.weight(1f), value = "28", unit = "", label = "AGE")
                }
            }

            // BMI Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Card),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Body Mass Index", fontSize = 14.sp, color = TextSub, fontWeight = FontWeight.Bold)
                                Text("22.9", fontSize = 28.sp, fontWeight = FontWeight.Black, color = TextMain)
                            }
                            Surface(
                                color = Color(0xFFDCFCE7),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    "Normal Weight",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    color = Color(0xFF166534),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Progress Bar BMI
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Input)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .fillMaxHeight()
                                    .background(Primary, CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("UNDERWEIGHT", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                            Text("NORMAL", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                            Text("OVERWEIGHT", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Activity Summary
            item {
                Text("Activity Summary", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextMain)
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard(Modifier.weight(1f), "Calories", "12,450", "Kcal burned total", Color(0xFFFFF7ED), Color(0xFFEA580C), R.drawable.icfire)
                        SummaryCard(Modifier.weight(1f), "Workouts", "42", "Sessions completed", Color(0xFFEFF6FF), Color(0xFF2563EB), R.drawable.icheartrate)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard(Modifier.weight(1f), "Streak", "5 Days", "Keep it up!", Color(0xFFFEF2F2), Color(0xFFDC2626), R.drawable.icfire)
                        SummaryCard(Modifier.weight(1f), "Form Score", "92%", "Avg. Accuracy", Color(0xFFF5F3FF), Color(0xFF7C3AED), R.drawable.jump)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun BiometricCard(modifier: Modifier, value: String, unit: String, label: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Input.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextMain)
                if (unit.isNotEmpty()) {
                    Text(unit, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub, modifier = Modifier.padding(bottom = 2.dp, start = 2.dp))
                }
            }
            Text(label, fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SummaryCard(modifier: Modifier, label: String, value: String, desc: String, bgColor: Color, iconColor: Color, iconRes: Int) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.dp).background(bgColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(iconRes), null, tint = iconColor, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextMain)
            }
            Spacer(Modifier.height(12.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = TextMain)
            Text(desc, fontSize = 11.sp, color = TextSub)
        }
    }
}