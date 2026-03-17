package dev.ferynnd.tugasakhir.ui.layouts

import android.R.attr.iconTint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.model.HistoryExercise
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.ExerciseViewModel
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Input
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colEmail
import dev.ferynnd.tugasakhir.ui.theme.colFire
import dev.ferynnd.tugasakhir.ui.theme.colHeart
import dev.ferynnd.tugasakhir.ui.theme.colLightning
import dev.ferynnd.tugasakhir.ui.theme.colTime
import kotlinx.datetime.LocalDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExerciseHistory(
    exerciseViewModel: ExerciseViewModel,
    supabaseClient: SupabaseClient,
    navController: NavController
) {

    val user = remember { supabaseClient.auth.currentUserOrNull() }

    LaunchedEffect(Unit) {
        user?.id?.let {
            exerciseViewModel.getHistoryExercise(it)
        }
    }

    val historyList = exerciseViewModel.historyList

    val totalCalories = exerciseViewModel.totalCaloriesToday
    val totalDuration = exerciseViewModel.totalMinutesToday
    val totalReps = exerciseViewModel.totalRepsToday

    var visibleItemCount by remember { mutableIntStateOf(6) }

    Scaffold(
        containerColor = Background, // Pastikan Background adalah warna gelap
    ) { paddingValues ->
        // Gunakan LazyColumn sebagai kontainer utama daripada Column + verticalScroll
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
        ) {

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Row ( horizontalArrangement = Arrangement.spacedBy(5.dp) ) {
                    Text(
                        text = "RIWAYAT",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSub
                    )
                    Text(
                        text = "LATIHAN",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("AKTIFITAS HARIAN", fontWeight = FontWeight.Black, color = White)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Gunakan warna Lime Green atau warna aksen yang konsisten
                    SummaryCard(
                        R.drawable.icfire,
                        colFire, // Ganti ke Lime Green agar sesuai tema
                        totalCalories.toString(),
                        "Kal",
                        modifier = Modifier.weight(1f).height(120.dp)
                    )
                    SummaryCard(
                        R.drawable.jump,
                        colLightning,
                        totalReps.toString(),
                        "Reps",
                        modifier = Modifier.weight(1f).height(120.dp)
                    )
                    SummaryCard(
                        R.drawable.icoclock,
                        colHeart,
                        totalDuration.toString(),
                        "Min",
                        modifier = Modifier.weight(1f).height(120.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("AKTIFITAS TERAKHIR", fontWeight = FontWeight.Black, color = White)
                }
            }

            if (historyList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Belum ada data riwayat", color = Color.Gray)
                    }
                }
            } else {
                // Langsung gunakan items di dalam LazyColumn utama
                val displayedHistory = historyList.take(visibleItemCount)

                items(displayedHistory) { history ->
                    HistoryCard(history, navController)
                }

                // Tampilkan tombol LOAD MORE jika masih ada item yang belum tampil
                if (visibleItemCount < historyList.size) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp), // Beri jarak atas-bawah agar tidak sesak
                            horizontalArrangement = Arrangement.Center // INI KUNCINYA agar di tengah
                        ) {
                            Surface(
                                onClick = { visibleItemCount += 5 },
                                color = Primary, // Menggunakan Lime Green tema baru Anda
                                shape = RoundedCornerShape(50.dp),
                                shadowElevation = 4.dp // Sedikit bayangan agar pop-out
                            ) {
                                Text(
                                    text = "LIHAT LEBIH",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold, // Lebih tebal agar sporty
                                    letterSpacing = 1.sp // Jarak antar huruf untuk kesan modern
                                )
                            }
                        }
                    }
                }
            }

            // Spacer akhir agar scroll tidak mepet bawah
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun SummaryCard( icon: Int, iconTint : Color, value: String, label : String , modifier : Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Card),
    ) {
        val imageVector = ImageVector.vectorResource(id = icon)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = iconTint.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = TextStyle(color = White ,fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label.uppercase(),
                style = TextStyle(color = TextSub, fontSize = 14.sp)
            )
        }
    }
}
@Composable
fun HistoryCard(history: HistoryExercise, navController: NavController) {

    val date = history.createdAt?.substring(0, 10) ?: "-"
    val exerciseIcon = getExerciseIcon(history.codeExercise)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("trainingSummary/${history.id}")
            },
        shape = RoundedCornerShape(12.dp), // Lebih membulat agar modern
        colors = CardDefaults.cardColors(containerColor = Card), // Dark surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Icon Section dengan Glassmorphism style
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = exerciseIcon),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Primary // Gunakan warna Lime Green agar konsisten
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Info Section
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = getExerciseName(history.codeExercise).uppercase(),
                        style = TextStyle(
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 3. Stats Row (Lebih bersih tanpa background berat)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniInfoItem(R.drawable.icfire, "${history.totalCalorie ?: 0} kcal", colFire)
                    MiniInfoItem(R.drawable.jump, "${history.reps ?: 0} reps", colLightning)
                    MiniInfoItem(R.drawable.icoclock, "${history.duration?.split(":")?.get(1) ?: 0} min",
                        colHeart
                    )
                }
            }
        }
    }
}

@Composable
fun MiniInfoItem(iconRes: Int, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = color.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}


/* Function untuk menentukan icon berdasarkan exercise */
fun getExerciseIcon(code: String?): Int {
    return when (code) {
        "PUSH_UP" -> R.drawable.icpushup
        "SQUAT" -> R.drawable.icsquat
        "SIT_UP" -> R.drawable.icsitup
        else -> R.drawable.bgta
    }
}

fun getExerciseName(code: String?): String {
    return when (code) {
        "PUSH_UP" -> "Push Up"
        "SQUAT" -> "Squat"
        "SIT_UP" -> "Sit Up"
        else -> "-"
    }
}