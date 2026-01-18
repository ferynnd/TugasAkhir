package dev.ferynnd.tugasakhir.ui.layouts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.model.dummyExercises
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.components.ExerciseListDialog
import dev.ferynnd.tugasakhir.ui.theme.*
import io.github.jan.supabase.auth.auth


@Composable
fun HomeScreen(navController: NavController) {
    val user = remember { SupabaseClient.client.auth.currentUserOrNull() }
    val displayUsername = user?.userMetadata?.get("username")?.toString()?.replace("\"", "") ?: "User"

    var showExerciseDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = White,
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

            item {
                Column {
                    Text("QUICK ACTIONS", fontWeight = FontWeight.Black, color = TextMain)
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        onClick = {showExerciseDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {

                            Image(
                                painter = painterResource(R.drawable.bgta),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.35f))
                            )

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart) // ⬅️ INI KUNCINYA
                                    .padding(20.dp)
                            ) {

                                Surface(
                                    color = Primary,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "RECOMMENDED",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                        color = White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    "LET'S\nEXERCISE",
                                    color = White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    lineHeight = 28.sp
                                )

                                Spacer(Modifier.height(12.dp))

                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(containerColor = White),
                                    shape = RoundedCornerShape(50),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    Text("Click Now", color = TextMain, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.width(8.dp))
                                    CustomIcon(
                                        R.drawable.icarrowr,
                                        null,
                                        tint = White,
                                        backgroundColor = Primary,
                                        cornerRadius = 50.dp,
                                        padding = 2.dp,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    if (showExerciseDialog) {
        ExerciseListDialog(
            exercises = dummyExercises,
            onDismiss = { showExerciseDialog = false },
            onSelect = { exercise ->
                showExerciseDialog = false
                navController.navigate("cameraScan/${exercise.type.name}")
            }
        )
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
    }
}

@Composable
fun BMIStatusCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = Background),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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

            Card(modifier = Modifier.size(100.dp), RoundedCornerShape(18.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE8D9C4))
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomIcon(
                            iconRes = R.drawable.jump,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }
            }
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .background(Brush.verticalGradient(listOf(Primary, Color(0xFF991B1B))), RoundedCornerShape(18.dp))
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
            Card(
                modifier = Modifier.weight(1f).height(140.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Background)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomIcon(R.drawable.icheartrate, null, tint = Color(0xFF3B82F6), backgroundColor = Color(0xFFDBEAFE), cornerRadius = 50.dp, padding = 6.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("EXERCISE", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                    Text("3", fontSize = 24.sp, color = TextMain, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
