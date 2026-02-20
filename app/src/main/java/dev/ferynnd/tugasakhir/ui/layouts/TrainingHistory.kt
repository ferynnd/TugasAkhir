package dev.ferynnd.tugasakhir.ui.layouts

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
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
import dev.ferynnd.tugasakhir.ui.theme.Input
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colEmail
import dev.ferynnd.tugasakhir.ui.theme.colWarning

@Composable
fun ExerciseHistory( navController: NavController, exerciseViewModel: ExerciseViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) { append("Riwayat ") }
                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Red)) { append("Latihan") }
            },
            fontSize = 32.sp,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Summary Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryCard(R.drawable.icfire, Color(0xFFFF6B6B),"1400", "Kal")
            SummaryCard(R.drawable.icoclock,Color(0xFF4169E1), "120", "Min")
            SummaryCard(R.drawable.jump,Color(0xFF00C853), "300", "Reps")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recent Activity",
            style = TextStyle(color = Black, fontWeight = FontWeight.Bold, fontSize = 18.sp),
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
fun SummaryCard( icon: Int, iconTint : Color, value: String, label : String) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .width(130.dp),
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
                style = TextStyle(color = Black,fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Text(
                text = label,
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
        shape = RoundedCornerShape(12.dp),
         border = BorderStroke(1.dp, Input.copy(0.9f)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    .background(TextSub.copy(0.15f))
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
                        style = TextStyle(color = Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    )
                    Surface(
                        color = colEmail.copy(0.8f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Kamis, 23 Januari 2026",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = TextStyle(color = White, fontSize = 10.sp)
                        )
                    }
                }

                Text(text = "Legs & Glutes", color = Color.Gray, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(painterResource(id = R.drawable.icfire), null, tint = Color.Red, modifier = Modifier.size(14.dp))
                        Text("200 Kal", fontSize = 12.sp , color = TextSub)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(painterResource(id = R.drawable.icoclock), null, tint = Color.Red, modifier = Modifier.size(14.dp))
                        Text("3 Min ", fontSize = 12.sp , color = TextSub)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(painterResource(id = R.drawable.jump), null, tint = Color.Red, modifier = Modifier.size(14.dp))
                        Text("30 Reps", fontSize = 12.sp , color = TextSub)
                    }
                }
            }
        }
    }
}