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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import dev.ferynnd.tugasakhir.ui.theme.Black
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors

@ExperimentalMaterial3Api
@Composable
fun TrainingSummary() {
    Scaffold(
        bottomBar = {
            Button(
                onClick = { /* Finish action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF3333)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Finish", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // Header
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Push Up",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "18:42 • 10 Des 2025",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { /* Back */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )

            // Summary Cards (Reuse previous component)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryCard(R.drawable.icfire, Color(0xFFFF6B6B),"1400", "Kal")
                SummaryCard(R.drawable.icoclock,Color(0xFF4169E1), "120", "Min")
                SummaryCard(R.drawable.jump,Color(0xFF00C853), "300", "Reps")
            }

            Spacer(modifier = Modifier.height(16.dp))
            CorrectionFormCard()

            Spacer(modifier = Modifier.height(24.dp))
            LastSessionSection()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CorrectionFormCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Correction Form",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Surface(
                    color = Color(0xFFC8E6C9),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Completed",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = TextStyle(color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Video/Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Info Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star, // Ganti dengan icon clipboard merah
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text...",
                    style = TextStyle(fontSize = 11.sp, color = Color.DarkGray, lineHeight = 16.sp)
                )
            }
        }
    }
}


@Composable
fun LastSessionSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "Vs. Last Session (Des 24)",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Item List
        repeat(3) {
            ComparisonItem()
            if (it < 2) Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ComparisonItem() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Avg. Tempo", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "Second per rep", color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "+5%", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "Improved", color = Color.Gray, fontSize = 10.sp)
            }
        }
    }
}