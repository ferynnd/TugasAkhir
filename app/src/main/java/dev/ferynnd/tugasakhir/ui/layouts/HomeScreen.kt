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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.model.CategoryBmi
import dev.ferynnd.tugasakhir.data.model.dummyExercises
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.ExerciseViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.components.ExerciseListDialog
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import dev.ferynnd.tugasakhir.ui.theme.*
import io.github.jan.supabase.auth.auth


@Composable
fun HomeScreen(
    navController: NavController,
    exerciseViewModel: ExerciseViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    supabaseClient: SupabaseClient
) {
    val user = remember { supabaseClient.auth.currentUserOrNull() }
    LaunchedEffect(user?.id) {
        if (user != null) {
            userViewModel.getProfile(user.id)
            userViewModel.getUserBMI(user.id)
            exerciseViewModel.getHistoryHome(user.id)

        }
    }

    val displayUsername by remember { derivedStateOf { userViewModel.fullName } }
    val displayAvatar by remember { derivedStateOf { userViewModel.avatar } }
    val displayBMIValue by remember { derivedStateOf { userViewModel.bmiResult } }
    val displayBMICategory by remember { derivedStateOf { userViewModel.bmiCategory } }

    var showExerciseDialog by remember { mutableStateOf(false) }

    val todayCalories by remember { derivedStateOf { exerciseViewModel.totalCaloriesToday } }
    val todayExercise by remember { derivedStateOf { exerciseViewModel.totalExerciseToday } }


    Scaffold(
        containerColor = Background,
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

            BMIStatusCard(
                displayBMIValue = displayBMIValue,
                displayBMICategory = CategoryBmi.valueOf(displayBMICategory)
            )
            ProgressSection(
                todayCalories = todayCalories,
                todayExercise = todayExercise
            )
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
            lottieRes = R.raw.warning,
            title = "Lengkapi Profil BMI",
            message = "Lengkapi data BMI untuk mengetahui status berat badan Anda.",
            confirmText = "Lengkapi",
            colorBg = colWarning.copy(alpha = 0.15f),
            dismissText = "Nanti Saja",
            onConfirm = {
                userViewModel.showBmiWarning = false
                navController.navigate("editBMI")
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
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                        .border(2.dp, Primary, CircleShape),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.placeholder), error = painterResource(R.drawable.placeholder)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("SELAMAT DATANG", fontSize = 14.sp, color = TextSub, fontWeight = FontWeight.Bold)
                Text(fullname.uppercase(), fontSize = 22.sp, color = White, fontWeight = FontWeight.Black)
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
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Card)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 40.dp, y = (40).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Primary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomIcon(iconRes = R.drawable.icheartrate, contentDescription = null, tint = Primary, backgroundColor = Primary.copy(alpha = 0.1f), cornerRadius = 8.dp, padding = 4.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("STATUS TUBUH", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextSub)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(String.format("%.1f", displayBMIValue), fontSize = 42.sp, fontWeight = FontWeight.Bold, color = White)
                        Text("BMI", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextSub, modifier = Modifier.padding(bottom = 3.dp, start = 5.dp))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        color = Primary,
                        shape = RoundedCornerShape(100)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(painterResource(R.drawable.iccheck), null, tint = Black, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("BERAT ${displayBMICategory.toString()}" , fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Black)
                        }
                    }
                }

                Card(
                    modifier = Modifier.size(110.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = Primary.copy(alpha = 0.1f),
                                radius = size.minDimension / 1.5f,
                                center = Offset(size.width, 0f)
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painterResource(R.drawable.fitlenlogo),
                                null,
                                tint = Primary,
                                modifier = Modifier.size(60.dp).padding(bottom = 6.dp)

                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ProgressSection(
    todayCalories : Int,
    todayExercise : Int
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("PROGRES HARIAN", fontWeight = FontWeight.Black, color = White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .background(Card, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 20.dp, y = (30).dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    colFire.copy(alpha = 0.07f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Icon(painterResource(R.drawable.icfire), null, tint = colFire.copy(0.1f), modifier = Modifier.size(50.dp).align(Alignment.BottomEnd))
                Column {
                    CustomIcon(R.drawable.icfire, null, tint = colFire, backgroundColor = colFire.copy(0.3f), cornerRadius = 50.dp, padding = 6.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("KALORI TERBAKAR", fontSize = 12.sp, color = TextSub, fontWeight = FontWeight.Bold)
                    Text("${todayCalories} Kal", fontSize = 24.sp, color = White, fontWeight = FontWeight.Black)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .background(Card, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 20.dp, y = (30).dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    colHeart.copy(alpha = 0.07f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Icon(painterResource(R.drawable.icheartrate), null, tint = colHeart.copy( alpha = 0.1f), modifier = Modifier.size(50.dp).align(Alignment.BottomEnd))
                Column {
                    CustomIcon(R.drawable.icheartrate, null, tint = colHeart, backgroundColor = colHeart.copy(0.3f), cornerRadius = 50.dp, padding = 6.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("LATIHAN", fontSize = 12.sp, color = TextSub, fontWeight = FontWeight.Bold)
                    Text(todayExercise.toString(), fontSize = 24.sp, color = White, fontWeight = FontWeight.Black)
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
        Text("AKSI CEPAT", fontWeight = FontWeight.Black, color = White)
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            onClick = { onActionClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = painterResource(R.drawable.bgta),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {

                    Surface(
                        color = Primary,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "REKOMENDASI",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                            color = Black,
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

                    Surface(
                        onClick = { onActionClick() },
                        color = Primary, // Putih agar kontras di atas hitam
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp).height(60.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MULAI SEKARANG",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // Ikon lingkaran hitam kecil
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color.Black, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icarrowr),
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
            }
            }
        }
    }
}
