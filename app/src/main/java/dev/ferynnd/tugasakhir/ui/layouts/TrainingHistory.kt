package dev.ferynnd.tugasakhir.ui.layouts

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.common.io.Files.append
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.viewmodel.ExerciseViewModel
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.TextSub

@Composable
fun ExerciseHistory( navController: NavController, exerciseViewModel: ExerciseViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Top Navigation
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.background(Color.LightGray, CircleShape))
        }

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) { append("Riwayat ") }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) { append("Latihan") }
            },
            fontSize = 28.sp,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Summary Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryCard("230")
            SummaryCard("230")
            SummaryCard("230")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recent Activity",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // History List
        LazyColumn {
            items(4) {
                HistoryCard()
            }
        }
    }
}

@Composable
fun SummaryCard(calories: String) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(130.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icfire), // Ganti dengan icon api Anda
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = calories,
                style = TextStyle(color = Black,fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Text(
                text = "Kcal",
                style = TextStyle(color = TextSub, fontSize = 12.sp)
            )
        }
    }
}

@Composable
fun HistoryCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD9D9D9))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Push Up",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    )
                    Surface(
                        color = Color(0xFFC8E6C9),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Completed",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = TextStyle(color = Color(0xFF2E7D32), fontSize = 10.sp)
                        )
                    }
                }

                Text(text = "Legs & Glutes", color = Color.Gray, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(id = R.drawable.icfire), null, tint = Color.Red, modifier = Modifier.size(14.dp))
                    Text(" 20  | ", fontSize = 12.sp)
                    Icon(painterResource(id = R.drawable.icfire), null, tint = Color.Red, modifier = Modifier.size(14.dp))
                    Text(" 100  | ", fontSize = 12.sp)
                    Icon(painterResource(id = R.drawable.icfire), null, tint = Color.Red, modifier = Modifier.size(14.dp))
                    Text(" 5min", fontSize = 12.sp)
                }
            }
        }
    }
}