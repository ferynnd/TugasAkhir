package dev.ferynnd.tugasakhir.ui.layouts

import android.R.attr.iconTint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.model.HistoryExercise
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.ExerciseViewModel
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Input
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.colEmail

@Composable
fun ExerciseHistory(
    exerciseViewModel: ExerciseViewModel,
    supabaseClient: SupabaseClient
) {

    val user = remember { supabaseClient.auth.currentUserOrNull() }

    LaunchedEffect(Unit) {
        user?.id?.let {
            exerciseViewModel.getHistoryExercise(it)
        }
    }

    val historyList = exerciseViewModel.historyList

    // 🔥 HITUNG SUMMARY OTOMATIS
    val totalCalories = historyList.sumOf { it.totalCalorie ?: 0 }
    val totalDuration = historyList.sumOf {
        it.duration?.substringBefore(":")?.toIntOrNull() ?: 0
    }
    val totalReps = historyList.sumOf { it.reps ?: 0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text(
            text = "Riwayat Latihan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 20.dp),
            color = Black
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryCard(
                R.drawable.icfire,
                Color(0xFFFF6B6B),
                totalCalories.toString(),
                "Kal",
                modifier = Modifier.weight(1f).height(120.dp)
            )
            SummaryCard(
                R.drawable.icoclock,
                Color(0xFF4169E1),
                totalDuration.toString(),
                "Min",
                modifier = Modifier.weight(1f).height(120.dp)
            )
            SummaryCard(
                R.drawable.jump,
                Color(0xFF00C853),
                totalReps.toString(),
                "Reps",
                modifier = Modifier.weight(1f).height(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recent Activity",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (historyList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada data riwayat", color = Black)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(historyList.size) { index ->
                    val history = historyList[index]
                    HistoryCard(history)
                }
            }
        }
    }
}


@Composable
fun SummaryCard( icon: Int, iconTint : Color, value: String, label : String , modifier : Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor =  Input.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
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
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = TextStyle(color = Black,fontWeight = FontWeight.Bold, fontSize = 16.sp)
            )
            Text(
                text = label,
                style = TextStyle(color = TextSub, fontSize = 12.sp)
            )
        }
    }
}
@Composable
fun HistoryCard(history: HistoryExercise) {
//
    val date = history.createdAt?.substring(0, 10) ?: "-"
    val minutes = history.duration?.substringBefore(":") ?: "0"

    val exerciseIcon = getExerciseIcon(history.codeExercise)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.07f)),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Exercise Icon
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = exerciseIcon),
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = getExerciseName(history.codeExercise),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Black

                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colEmail
                    ) {
                        Text(
                            text = date,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    InfoItem(R.drawable.icfire, "${history.totalCalorie ?: 0} Kal")

                    InfoItem(R.drawable.icoclock, "$minutes Min")

                    InfoItem(R.drawable.jump, "${history.reps ?: 0} Reps")
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFFFF6B6B)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Gray
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