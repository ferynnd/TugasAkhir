package dev.ferynnd.tugasakhir.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.components.*
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colEmail

@Composable
fun TrainingDetail(navController: NavController) {
    val scrollState = rememberScrollState()

     Scaffold(
        containerColor = Background,
          topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                       .background(Color(0xFFE5E5E5))

                ) {
                    CustomIcon(
                        R.drawable.icarrowr,
                        contentDescription = null,
                        tint = TextSub,
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer(scaleX = -1f) // Flip horizontal

                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Detail Latihan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

            }
        },
    ) { paddingValues ->

         Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
             Column(
                 modifier = Modifier
                     .fillMaxSize()
                     .verticalScroll(scrollState)
             ) {
                 // Header Image Section
                 HeaderImageSection(
                     imageRes = R.drawable.bgta,
                 )

                 // Content Section
                 Column(
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(horizontal = 20.dp)
                         .clip(
                             RoundedCornerShape(
                                 topStart = 16.dp,
                                 topEnd = 16.dp,
                                 bottomStart = 0.dp,
                                 bottomEnd = 0.dp
                             )
                         )
                 ) {
                     Spacer(modifier = Modifier.height(16.dp))

                     // Title
                     Text(
                         text = "Push Up",
                         fontSize = 28.sp,
                         fontWeight = FontWeight.Bold,
                         color = Color.Black
                     )

                     Spacer(modifier = Modifier.height(8.dp))

                     // Stats Row
                     StatsRow(
                         calories = "230",
                         duration = "15",
                         level = "10"
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // Description Section
                     DescriptionSection(
                         description = "The push-up is a total-body functional move that serves as a barometer of fitness. While primarily targeting the chest and arms, it requires coordinated engagement from your core and lower body to maintain a rigid plank, making it a superior builder of metabolic health and foundational strength."
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // Muscles Worked Section
                     MusclesWorkedSection(
                         primaryMuscles = listOf("Chest", "Triceps"),
                         secondaryMuscles = listOf("Shoulders", "Core")
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // How to Perform Section
                     HowToPerformSection(
                         steps = listOf(
                             "The Setup" to "Place hands slightly wider than shoulder-width apart. Maintain a neutral spine by engaging your core, tucking your chin and squeezing your glutes to align your body from heels to head.",
                             "The Descent" to "Inhale as you lower your body, keeping elbows at a 45-degree angle to your torso. Aim to descend until your chest nearly touches the floor without losing core tension.",
                             "The Push" to "Exhale forcefully as you drive through your palms, screwing your hands into the floor, returning to the starting position without locking out elbows aggressively."
                         )
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // Common Mistakes Section
                     CommonMistakesSection(
                         mistakes = listOf(
                             "Sagging Hips\nThis often leads to avoid lower back strain.",
                             "Elbows Flared Too Wide\nLock your shoulders in their sockets to protect joints.",
                             "Short Range of Motion\nGo chest-to-floor for maximum muscle fiber activation."
                         )
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // Benefits Section
                     BenefitsSection(
                         benefits = listOf(
                             "Total Upper Body Strength" to "Engages chest, shoulders, triceps and lats builds.",
                             "Metabolic Boost" to "Burns significant calories by engaging massive muscle groups.",
                             "Posture Correction" to "Strengthens core and back to counteract desk-bound slouching."
                         )
                     )

                     Spacer(modifier = Modifier.height(100.dp)) // Space for button
                 }
             }

             // Floating Button at Bottom
             Box(
                 modifier = Modifier
                     .align(Alignment.BottomCenter)
                     .background(White)
                     .fillMaxWidth()
                     .drawBehind {
                         drawLine(
                             color = TextSub.copy(0.1f),
                             start = Offset(0f, 0f),
                             end = Offset(size.width, 0f),
                             strokeWidth = 1.dp.toPx()
                         )
                     }
                     .padding(horizontal = 20.dp, vertical = 16.dp)

             ) {
                 Button(
                     onClick = {},
                     modifier = Modifier.fillMaxWidth().height(60.dp),
                     colors = ButtonDefaults.buttonColors(containerColor = Primary),
                     shape = RoundedCornerShape(12.dp),
                     contentPadding = PaddingValues(0.dp)
                 ) {
                     Row(verticalAlignment = Alignment.CenterVertically) {
                         Icon(
                             painter = painterResource(id = R.drawable.icplay),
                             contentDescription = null,
                             modifier = Modifier.size(24.dp),
                             tint = White
                         )
                         Spacer(Modifier.width(10.dp))
                         Text(
                             "START EXERCISE",
                             fontWeight = FontWeight.Bold,
                             fontSize = 16.sp,
                             color = White
                         )
                     }
                 }
             }
         }
    }
}

@Composable
fun HeaderImageSection(
    imageRes: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape( 16.dp))
            .height(250.dp)

    ) {
        // Background Image
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun StatsRow(
    calories: String,
    duration: String,
    level: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        StatItem(
            modifier = Modifier.weight(1f),
            icon = R.drawable.icfire,
            value = calories,
            label = "KCAL",
            iconTint = Color(0xFFFF6B6B)
        )

        StatItem(
            modifier = Modifier.weight(1f),
            icon = R.drawable.icoclock,
            value = duration,
            label = "MINS",
            iconTint = Color(0xFF4169E1)
        )

        StatItem(
            modifier = Modifier.weight(1f),
            icon = R.drawable.icbolt,
            value = level,
            label = "MINS",
            iconTint = Color(0xFF00C853)
        )
    }
}

@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    icon: Int,
    value: String,
    label: String,
    iconTint: Color
) {
    Column(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.03f))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}


// Section Title Component
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .background(
                    Primary,
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

// Description Section
@Composable
fun DescriptionSection(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = "Deskripsi")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = description,
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.8f),
            lineHeight = 20.sp
        )
    }
}

// Muscles Worked Section
@Composable
fun MusclesWorkedSection(
    primaryMuscles: List<String>,
    secondaryMuscles: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = " Otot Yang Digunakan ")

        Spacer(modifier = Modifier.height(16.dp))

        // Ilustrasi Otot (bisa diganti dengan gambar)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Color.Black.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Muscle Illustration",
                color = Color.Black.copy(alpha = 0.3f),
                fontSize = 12.sp
            )
        }
    }
}


// How to Perform Section
@Composable
fun HowToPerformSection(
    steps: List<Pair<String, String>>, // Pair of (title, description)
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = "Cara Melakukan")

        Spacer(modifier = Modifier.height(16.dp))

        steps.forEachIndexed { index, (title, description) ->
            StepItem(
                stepNumber = index + 1,
                title = title,
                description = description
            )

            if (index < steps.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StepItem(
    stepNumber: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
         .padding(vertical = 10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Primary.copy(0.8f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )
        }
    }
}

// Common Mistakes Section
@Composable
fun CommonMistakesSection(
    mistakes: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = "Kesalahan Umum")

        Spacer(modifier = Modifier.height(16.dp))

        mistakes.forEachIndexed { index, mistake ->
            MistakeItem(mistake)

            if (index < mistakes.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MistakeItem(mistake: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border( 1.dp, Primary.copy(0.2f), shape = RoundedCornerShape(8.dp) )
            .background(
                Primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(28.dp)
                .background(
                    Primary.copy(0.8f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

         Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "title",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = mistake,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.7f),
                lineHeight = 16.sp
            )
        }
    }
}

// Benefits Section
@Composable
fun BenefitsSection(
    benefits: List<Pair<String, String>>, // Pair of (title, description)
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = "Manfaat")

        Spacer(modifier = Modifier.height(16.dp))

        benefits.forEach { (title, description) ->
            BenefitItem(title, description)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun BenefitItem(
    title: String,
    description: String
) {
    Row(
         modifier = Modifier
            .fillMaxWidth()
            .border( 1.dp, TextSub.copy(0.1f), shape = RoundedCornerShape(8.dp) )
            .background(
                TextSub.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icplay),
            contentDescription = null,
            tint = Color(0xFF00C853),
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .size(30.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f),
                lineHeight = 18.sp
            )
        }
    }
}
