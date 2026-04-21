package dev.ferynnd.tugasakhir.ui.layouts

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.data.viewmodel.ExerciseViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.White

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrainingSummary(
    historyId: Int,
    viewModel: ExerciseViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    LaunchedEffect(historyId) {
        viewModel.getSummaryData(historyId)
    }

    val current = viewModel.currentHistory
    val last = viewModel.lastHistory

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "RINGKASAN LATIHAN",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )

                Surface(
                    color = Primary.copy(0.3f),
                    border = BorderStroke(1.5.dp, Primary),
                    shape = RoundedCornerShape(100),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CustomIcon(
                            R.drawable.icoclock,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                        val date = current?.createdAt?.substring(0, 10) ?: "-"
                        Text(
                            text = date,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = White
                        )

                    }
                }
            }
        },
        containerColor = Background,
        bottomBar = {
            Column(modifier = Modifier.padding(20.dp)) {
                Button(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("TUTUP", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            StatsRow(
                calories = "${current?.totalCalorie ?: 0}",
                duration = "${timeToMinutes(current?.duration)}",
                reps = "${current?.reps ?: 0}"
            )

            Text(
                text = "VS SESI TERAKHIR",
                style = TextStyle(color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Card, RoundedCornerShape(12.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    ComparisonRow(
                        label = "Reps",
                        currentVal = "${current?.reps ?: 0} reps",
                        lastVal = "${last?.reps ?: 0} reps",
                        icon = R.drawable.jump,
                        currentRaw = current?.reps ?: 0,
                        lastRaw = last?.reps ?: 0
                    )
                    ComparisonRow(
                        label = "Durasi",
                        currentVal = "${timeToMinutes(current?.duration.toString())} min",
                        lastVal = "${timeToMinutes(last?.duration.toString())} min",
                        icon = R.drawable.icoclock,
                        currentRaw = current?.duration?.split(":")?.get(1)?.toInt() ?: 0,
                        lastRaw = last?.duration?.split(":")?.get(1)?.toInt() ?: 0
                    )
                    ComparisonRow(
                        label = "Kalori Terbakar",
                        currentVal = "${current?.totalCalorie ?: 0} kcal",
                        lastVal = "${last?.totalCalorie ?: 0} kcal",
                        icon = R.drawable.icfire,
                        currentRaw = current?.totalCalorie ?: 0,
                        lastRaw = last?.totalCalorie ?: 0
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ComparisonRow(
    label: String,
    currentVal: String,
    lastVal: String,
    currentRaw: Int, // Kirim nilai angka untuk logika perbandingan
    lastRaw: Int,    // Kirim nilai angka untuk logika perbandingan
    icon: Int
) {
    // Logika UX: Hitung selisih untuk indikator performa
    val diff = currentRaw - lastRaw
    val isImproved = diff > 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White.copy(alpha = 0.07f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))


        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label.uppercase(),
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentVal,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = " vs $lastVal",
                    style = TextStyle(
                        color = Color.Gray.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        if (diff != 0) {
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isImproved) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = if (isImproved) Primary else Color(0xFFFF2D55),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (isImproved) "+${diff}" else "$diff",
                        color = if (isImproved) Primary else Color(0xFFFF2D55),
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = if (isImproved) "IMPROVED" else "LOWER",
                    color = if (isImproved) Primary.copy(alpha = 0.5f) else Color(0xFFFF2D55).copy(alpha = 0.5f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}