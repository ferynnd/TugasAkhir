package dev.ferynnd.tugasakhir.ui.layouts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.ui.theme.*
import io.github.jan.supabase.auth.auth
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.ferynnd.tugasakhir.data.model.CategoryBmi
import dev.ferynnd.tugasakhir.data.viewmodel.ExerciseViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.components.SmallFab

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    exerciseViewModel: ExerciseViewModel = hiltViewModel(),
    supabaseClient: SupabaseClient
) {
    val user = remember { supabaseClient.auth.currentUserOrNull() }

    LaunchedEffect(Unit) {
        user?.id?.let {
            userViewModel.loadProfileIfNeeded(it)
            exerciseViewModel.getHistoryProfile(it)
        }
    }

    // state ViewModel (Observe)
    val displayUsername by remember {
        derivedStateOf { userViewModel.fullName.uppercase() }
    }
    val avatar by remember {
        derivedStateOf { userViewModel.avatar }
    }
    val weightValue by remember {
        derivedStateOf { userViewModel.weightValue.toString() }
    }
    val ageValue by remember {
        derivedStateOf { userViewModel.ageValue}
    }
    val heightValue by remember {
        derivedStateOf { userViewModel.heightValue }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("PROFIL",  fontSize = 20.sp, fontWeight = FontWeight.Black, color = White)
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
           item {
                HeaderProfile(avatar, displayUsername)
           }
           item {
                Biometric(
                    heightValue = heightValue,
                    weightValue = weightValue.toString(),
                    ageValue = ageValue.toString()
                )
           }
           item {
                BMICard(
                    bmiValue = userViewModel.bmiResult,
                    categoryName = CategoryBmi.valueOf( userViewModel.bmiCategory)
                )
           }
           item {
                SummarySection(
                    calories = exerciseViewModel.totalCalories,
                    exercises = exerciseViewModel.totalExercises,
                    duration = exerciseViewModel.totalDurationFormatted,
                    streak = exerciseViewModel.currentStreak
                )
           }
        }
         ExpandableFab(
            onEdBmi = {
                navController.navigate("editBMI")
            },
            onEdProf = {
                navController.navigate("editProfile")
            }
        )
    }
}

// ==========================================================
//                   COMPONENT SECTION
// ==========================================================

@Composable
fun HeaderProfile(
    avatar: String,
    displayUsername: String,
)
{
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
             AsyncImage(
                 model = ImageRequest.Builder(LocalContext.current)
                        .data(avatar)
                        .size(128) // sesuai UI
                        .crossfade(false)
                        .build(),
                contentDescription = "Image Profil User",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(3.dp, Primary, CircleShape),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.placeholder), error = painterResource(R.drawable.placeholder)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text( displayUsername, fontSize = 24.sp, fontWeight = FontWeight.Black, color = White)
    }

}

@Composable
fun Biometric(
    heightValue : String,
    weightValue : String,
    ageValue : String
) {
    Column {
        Text("BIOMETRIK", fontSize = 18.sp, fontWeight = FontWeight.Black, color = White)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BiometricCard(modifier = Modifier.weight(1f), value = heightValue, unit = "cm", label = "TINGGI")
            BiometricCard(modifier = Modifier.weight(1f), value = weightValue, unit = "kg", label = "BERAT")
            BiometricCard(modifier = Modifier.weight(1f), value = ageValue, unit = "", label = "UMUR")
        }
    }
}

@Composable
fun BiometricCard(
    modifier: Modifier,
    value: String,
    unit: String,
    label: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = White)
                if (unit.isNotEmpty()) {
                    Text(unit.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub, modifier = Modifier.padding(bottom = 2.dp, start = 2.dp))
                }
            }
            Text(label, fontSize = 10.sp, color = Primary, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun BMICard(
    bmiValue : Double,
    categoryName : CategoryBmi
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "INDEKS MASSA TUBUH",
                        fontSize = 14.sp,
                        color = TextSub,
                        fontWeight = FontWeight.Bold
                    )
                    // Menampilkan 1 angka di belakang koma
                    Text(
                        text = String.format("%.1f", bmiValue),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = White
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("UNDERWEIGHT", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                Text("NORMAL", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                Text("OVERWEIGHT", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar dengan Indikator Dinamis
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val barWidth = maxWidth

                 val progress = when {
                    bmiValue < 18.5 ->  0.15f
                    bmiValue < 25.0 ->  0.5f
                    bmiValue < 30.0 ->  0.85f
                    else -> 0.95f
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Primary.copy(alpha = 0.3f),
                                    Primary
                                )
                            )
                        )
                )

                // Indikator (Garis Hitam)
                Box(
                    modifier = Modifier
                        .offset(x = (barWidth * progress) - 3.dp)
                        .width(6.dp)
                        .height(18.dp)
                        .align(Alignment.CenterStart)
                        .background(White.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                        .zIndex(1f)
                )
            }
        }
    }
}


@Composable
fun SummarySection(
    calories: Int,
    exercises: Int,
    duration: String,
    streak: Int
) {
    Column {
        Text("RINGKASAN AKTIFITAS", fontSize = 18.sp, fontWeight = FontWeight.Black, color = White)
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(Modifier.weight(1f), "Kalori", calories.toString(), " Total Kalori Terbakar", colFire.copy(alpha = 0.1f), colFire, R.drawable.icfire)
                SummaryCard(Modifier.weight(1f), "Latihan", exercises.toString(), " Latihan Terselesaikan ", colEmail.copy(alpha = 0.1f), colEmail, R.drawable.icheartrate)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(Modifier.weight(1f), "Streak", "${streak} Days", " Hari Berlatih Beruntun ", colLightning.copy(alpha = 0.1f), colLightning, R.drawable.lightning)
                SummaryCard(Modifier.weight(1f), "Durasi", "${duration}", " Waktu Berlatih  ", colHeart.copy(alpha = 0.1f), colHeart, R.drawable.icoclock)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier,
    label: String,
    value: String,
    desc: String,
    bgColor: Color,
    iconColor: Color,
    iconRes: Int
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(bgColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(iconRes), null, tint = iconColor, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text(label.uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextSub)
            }
            Spacer(Modifier.height(12.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = White)
            Text(desc, fontSize = 11.sp, color = TextSub, modifier = Modifier.padding( top = 5.dp))
        }
    }
}


@Composable
fun ExpandableFab(
    onEdBmi: () -> Unit,
    onEdProf: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(end = 20.dp, bottom = 20.dp)
        ) {

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    SmallFab(
                        icon = R.drawable.edbmi,
                        onClick = {
                            expanded = false
                            onEdBmi()
                        }
                    )
                    SmallFab(
                        icon = R.drawable.edprof,
                        onClick = {
                            expanded = false
                            onEdProf()
                        }
                    )
                }
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = Primary,
                shape = RoundedCornerShape(30),
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                modifier = Modifier.size(60.dp)
            ) {
                CustomIcon(
                    iconRes = if (expanded) R.drawable.cancel else R.drawable.ed,
                    contentDescription = null,
                    tint = Black
                )
            }
        }
    }
}
