package dev.ferynnd.tugasakhir.ui.layouts

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.model.CategoryBmi
import dev.ferynnd.tugasakhir.data.model.dummyExercises
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.components.ExerciseListDialog
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import dev.ferynnd.tugasakhir.ui.theme.*
import io.github.jan.supabase.auth.auth


@Composable
fun HomeScreen(navController: NavController, userViewModel: UserViewModel) {
    val user = remember { SupabaseClient.client.auth.currentUserOrNull() }
    LaunchedEffect(user?.id) {
        if (user != null) {
            userViewModel.getProfile(user.id)
            userViewModel.getUserBMI(user.id)
        }
    }

    val displayUsername by remember { derivedStateOf { userViewModel.fullName } }
    val displayAvatar by remember { derivedStateOf { userViewModel.avatar } }
    val displayBMIValue by remember { derivedStateOf { userViewModel.bmiResult } }
    val displayBMICategory by remember { derivedStateOf { userViewModel.bmiCategory } }
    Log.d("HomeScreen", "displayBMICat: $displayBMICategory")
//    val timestamp = remember(displayAvatar) { System.currentTimeMillis() }

    var showExerciseDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = White,
        topBar = { TopBarSection( fullname = displayUsername, avatar = displayAvatar) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            BMIStatusCard( displayBMIValue = displayBMIValue, displayBMICategory = CategoryBmi.valueOf(displayBMICategory) )
            ProgressSection()
            QuickAction(onActionClick = { showExerciseDialog = true })
        }
    }

    if (showExerciseDialog) {
        ExerciseListDialog(
            exercises = dummyExercises,
            onDismiss = { showExerciseDialog = false },
            onSelect = { exercise ->
                showExerciseDialog = false
                navController.navigate("cameraScan/${exercise.code}")
            }
        )
    }

    if (userViewModel.showBmiWarning) {
        LottieDialog(
            lottieRes = R.raw.warning, // Ganti dengan file lottie Anda
            title = "Lengkapi Profil BMI",
            message = "Lengkapi data BMI untuk mengetahui status berat badan Anda.",
            confirmText = "Lengkapi Sekarang",
            colorBg = colWarning.copy(alpha = 0.15f),
            dismissText = "Nanti Saja",
            onConfirm = {
                userViewModel.showBmiWarning = false
                navController.navigate("editBMI") // Arahkan ke halaman edit
            },
            onDismiss = {
                userViewModel.showBmiWarning = false
            }
        )
    }

}

@Composable
fun TopBarSection(
    avatar: String = "",
    fullname: String = "Guest",
    timestamp: Long = 0
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
                 AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(avatar)
                        .crossfade(true)
                        .size(120)
                        .build(),
                    contentDescription = "Image Profil User",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Primary.copy(alpha = 0.2f), CircleShape),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.placeholder), error = painterResource(R.drawable.placeholder)
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
                Text("SELAMAT DATANG", fontSize = 12.sp, color = TextSub, fontWeight = FontWeight.Bold)
                Text(fullname.uppercase(), fontSize = 18.sp, color = TextMain, fontWeight = FontWeight.Black)
            }
        }
    }
}

// ==========================================================
//                   COMPONENT SECTION
// ==========================================================


@Composable
fun BMIStatusCard( displayBMIValue: Double = 0.0, displayBMICategory: CategoryBmi ) {
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
                    Text("STATUS TUBUH", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(String.format("%.1f", displayBMIValue), fontSize = 38.sp, fontWeight = FontWeight.Black, color = TextMain)
                    Text("BMI", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextSub, modifier = Modifier.padding(bottom = 6.dp))
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
                        Text(displayBMICategory.toString(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF166534))
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
            Text("PROGRES HARIAN", fontWeight = FontWeight.Black, color = TextMain)
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
                    Text("KALORI TERBAKAR", fontSize = 10.sp, color = White.copy(0.8f), fontWeight = FontWeight.Bold)
                    Text("450 Kal", fontSize = 20.sp, color = White, fontWeight = FontWeight.Black)
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
                    Text("LATIHAN", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                    Text("3", fontSize = 24.sp, color = TextMain, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun QuickAction(
    onActionClick : () -> Unit
) {
    Column {
        Text("AKSI CEPAT", fontWeight = FontWeight.Black, color = TextMain)
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            onClick = { onActionClick() },
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
                            "REKOMENDASI",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                            color = White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "MARI\nBERLATIH",
                        color = White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 28.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onActionClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = White),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("Klik Sekarang", color = TextMain, fontWeight = FontWeight.Bold)
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
