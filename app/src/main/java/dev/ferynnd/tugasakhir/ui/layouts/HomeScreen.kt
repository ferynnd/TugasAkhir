package dev.ferynnd.tugasakhir.ui.layouts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.theme.*
import io.github.jan.supabase.auth.auth

@Composable
fun HomeScreen(navController: NavController) {

    val user = remember { SupabaseClient.client.auth.currentUserOrNull() }
    val displayUsername = user?.userMetadata?.get("full_name")?.toString()?.replace("\"", "") ?: "User"
    Scaffold(
        containerColor = Background,
        topBar = { TopBarSection( username = displayUsername) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(10.dp)) }

            item { BMIStatusCard() }

            item { ProgressSection() }

            item { QuickActionsSection(navController) }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun TopBarSection(
    username: String = "Guest"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Profile Picture with Online Dot
            Box {
                Image(
                    painter = painterResource(id = R.drawable.bgta), // Ganti sesuai drawable Anda
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Primary.copy(alpha = 0.2f), CircleShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color(0xFF22C55E), CircleShape)
                        .border(2.dp, White, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("LET'S MOVE", fontSize = 12.sp, color = TextSub, fontWeight = FontWeight.Bold)
                Text(username.uppercase(), fontSize = 18.sp, color = TextMain, fontWeight = FontWeight.Black)
            }
        }

        // Notification Icon
        //        Box(
        //            modifier = Modifier
        //                .size(40.dp)
        //                .background(White, CircleShape)
        //                .shadow(2.dp, CircleShape),
        //            contentAlignment = Alignment.Center
        //        ) {
        //            BadgedBox(badge = { Badge(containerColor = Primary) }) {
        //                Icon(Icons.Default.Notifications, contentDescription = null, tint = TextMain)
        //            }
        //        }
    }
}

@Composable
fun BMIStatusCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(15.dp, RoundedCornerShape(24.dp), spotColor = Color.LightGray.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomIcon(iconRes = R.drawable.icheartrate, contentDescription = null, tint = Primary, backgroundColor = Primary.copy(alpha = 0.1f), cornerRadius = 8.dp, padding = 4.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("HEALTH STATUS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("22.4", fontSize = 38.sp, fontWeight = FontWeight.Black, color = TextMain)
                    Text(" BMI", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextSub, modifier = Modifier.padding(bottom = 6.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Normal Weight Chip
                Surface(
                    color = Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.iccheck), null, tint = Color(0xFF166534), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("NORMAL WEIGHT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF166534))
                    }
                }
            }
            // Human Body Image Placeholder
            Image(
                painter = painterResource(id = R.drawable.bgta), // Tambahkan gambar anatomi tubuh
                contentDescription = null,
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun ProgressSection() {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("TODAY'S PROGRESS", fontWeight = FontWeight.Black, color = TextMain)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Calories Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .background(Brush.verticalGradient(listOf(Primary, Color(0xFF991B1B))), RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Icon(painterResource(R.drawable.icfire), null, tint = White.copy(0.3f), modifier = Modifier.size(60.dp).align(Alignment.BottomEnd))
                Column {
                    CustomIcon(R.drawable.icfire, null, tint = Primary, backgroundColor = White, cornerRadius = 50.dp, padding = 6.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("CALORIES BURNED", fontSize = 10.sp, color = White.copy(0.8f), fontWeight = FontWeight.Bold)
                    Text("450 Kcal", fontSize = 20.sp, color = White, fontWeight = FontWeight.Black)
                }
            }
            // Workouts Card
            Card(
                modifier = Modifier.weight(1f).height(140.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomIcon(R.drawable.icplay, null, tint = Color(0xFF3B82F6), backgroundColor = Color(0xFFDBEAFE), cornerRadius = 50.dp, padding = 6.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("WORKOUTS", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                    Text("3", fontSize = 24.sp, color = TextMain, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun QuickActionsSection(navController: NavController) {
    Column {
        Text("QUICK ACTIONS", fontWeight = FontWeight.Black, color = TextMain)
        Spacer(modifier = Modifier.height(16.dp))

        // Recommended Workout Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.bgta), // Gambar orang ikat tali sepatu
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.3f)))
            Column(modifier = Modifier.padding(20.dp).align(Alignment.CenterStart)) {
                Surface(color = Primary, shape = RoundedCornerShape(4.dp)) {
                    Text("RECOMMENDED", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text("START\nWORKOUT", color = White, fontSize = 28.sp, fontWeight = FontWeight.Black, lineHeight = 28.sp)
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { /* Nav to Camera */ },
                    colors = ButtonDefaults.buttonColors(containerColor = White),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Let's Go", color = TextMain, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    CustomIcon(R.drawable.icarrowr, null, tint = White, backgroundColor = Primary, cornerRadius = 50.dp, padding = 2.dp, modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar kartu
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f) // Membagi lebar sama rata
                        .height(160.dp)
                        .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = Color.LightGray.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { navController.navigate("trainingList") }
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xFFF3F4F6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(
                                iconRes = R.drawable.iclist, // Pastikan id resource benar
                                contentDescription = null,
                                tint = TextMain,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Workout List",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = Color.LightGray.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { /* Navigasi ke History */ }
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Icon History
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xFFF3F4F6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(
                                iconRes = R.drawable.ichistory, // Pastikan id resource benar
                                contentDescription = null,
                                tint = TextMain,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "History",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }
                }
            }
        }
    }
}