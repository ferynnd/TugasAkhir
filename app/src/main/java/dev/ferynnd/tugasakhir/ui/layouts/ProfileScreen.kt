package dev.ferynnd.tugasakhir.ui.layouts

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.ferynnd.tugasakhir.data.model.CategoryBmi
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.components.SmallFab

@Composable
fun ProfileScreen(navController: NavController,  userViewModel: UserViewModel) {
    val user = remember { SupabaseClient.client.auth.currentUserOrNull() }
    LaunchedEffect(user?.id) {
        if (user != null) {
            userViewModel.getProfile(user.id)
            userViewModel.getUserBMI(user.id)
        }
    }
    // state ViewModel (Observe)
    val displayUsername by remember {
        derivedStateOf { userViewModel.fullName.uppercase() }
    }
    val avatar by remember { derivedStateOf { userViewModel.avatar } }
    val weightValue = userViewModel.weightValue.toString()
    val ageValue = userViewModel.ageValue
    val heightValue = userViewModel.heightValue

    Scaffold(
        containerColor = White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Profile",  fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextMain)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            HeaderProfile(avatar, displayUsername)
            Biometric(
                heightValue = heightValue,
                weightValue = weightValue.toString(),
                ageValue = ageValue.toString()
            )
            BMICard(
                bmiValue = userViewModel.bmiResult,
                categoryName = CategoryBmi.valueOf( userViewModel.bmiCategory)
            )
            SummarySection()
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
                        .crossfade(true)
                        .size(300)
                        .build(),
                contentDescription = "Image Profil User",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Input.copy(0.7f), CircleShape),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.placeholder), error = painterResource(R.drawable.placeholder)
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
        Text( displayUsername, fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextMain)
    }

}

@Composable
fun Biometric(
    heightValue : String,
    weightValue : String,
    ageValue : String
) {
    Column {
        Text("Biometrics", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextMain)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BiometricCard(modifier = Modifier.weight(1f), value = heightValue, unit = "cm", label = "HEIGHT")
            BiometricCard(modifier = Modifier.weight(1f), value = weightValue, unit = "kg", label = "WEIGHT")
            BiometricCard(modifier = Modifier.weight(1f), value = ageValue, unit = "", label = "AGE")
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
        colors = CardDefaults.cardColors(containerColor = Input.copy(alpha = 0.7f)),
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
        colors = CardDefaults.cardColors(containerColor = Input.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Body Mass Index",
                        fontSize = 14.sp,
                        color = TextSub,
                        fontWeight = FontWeight.Bold
                    )
                    // Menampilkan 1 angka di belakang koma
                    Text(
                        text = String.format("%.1f", bmiValue),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                }

                // Status Badge (Warna berubah sesuai kategori)
                Surface(
                    color = if (categoryName == CategoryBmi.NORMAL) Color(0xFFDCFCE7) else Color(
                        0xFFFFEBEE
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        if (categoryName == CategoryBmi.NORMAL) {
                            Icon(
                                painter = painterResource(id = R.drawable.iccheck),
                                contentDescription = null,
                                tint = Color(0xFF166534),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                        }
                        Text(
                            text = categoryName.name,
                            color = if (categoryName == CategoryBmi.NORMAL) Color(0xFF166534) else Color(
                                0xFFC62828
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                                    Color(0xFF2196F3), // Blue
                                    Color(0xFF00C853), // Green
                                    Color(0xFFFFC107), // Yellow/Orange
                                    Color(0xFFF44336)  // Red
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
                        .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(2.dp))
                        .zIndex(1f)
                )
            }
        }
    }
}


@Composable
fun SummarySection() {
    Column {
        Text("Activity Summary", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextMain)
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(Modifier.weight(1f), "Calories", "1,450", "Kcal burned total", Color(0xFFEA580C).copy(alpha = 0.1f), Color(0xFFEA580C), R.drawable.icfire)
                SummaryCard(Modifier.weight(1f), "Exercise", "10", "Sessions completed", Color(0xFF2563EB).copy(alpha = 0.1f), Color(0xFF2563EB), R.drawable.icheartrate)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(Modifier.weight(1f), "Streak", "5 Days", "Keep it up!", Color(0xFFFFC700).copy(alpha = 0.1f), Color(0xFFFFC700), R.drawable.lightning)
                SummaryCard(Modifier.weight(1f), "Form Score", "92%", "Avg. Accuracy", Color(0xFF7C3AED).copy(alpha = 0.1f), Color(0xFF7C3AED), R.drawable.jump)
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
        colors = CardDefaults.cardColors(containerColor = Input.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(20.dp),
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
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                modifier = Modifier.size(70.dp)
            ) {
                CustomIcon(
                    iconRes = if (expanded) R.drawable.cancel else R.drawable.ed,
                    contentDescription = null,
                    tint = White
                )
            }
        }
    }
}
