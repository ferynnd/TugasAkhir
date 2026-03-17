package dev.ferynnd.tugasakhir.ui.layouts

import android.widget.Toast
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
import dev.ferynnd.tugasakhir.data.model.Additional
import dev.ferynnd.tugasakhir.data.model.dummyExercises
import dev.ferynnd.tugasakhir.ui.components.*
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colEmail
import dev.ferynnd.tugasakhir.ui.theme.colFire
import dev.ferynnd.tugasakhir.ui.theme.colHeart
import dev.ferynnd.tugasakhir.ui.theme.colLightning
import dev.ferynnd.tugasakhir.ui.theme.colTime

@Composable
fun TrainingDetail(navController: NavController, exerciseId: Int?) {

    val scrollState = rememberScrollState()

    val exercise = dummyExercises.find { it.id == exerciseId }

    if (exercise == null) {
        Toast.makeText(navController.context, "Exercise not found", Toast.LENGTH_SHORT).show()
        return
    }

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
                       .background(Primary)

                ) {
                    CustomIcon(
                        R.drawable.icarrowr,
                        contentDescription = null,
                        tint = Black,
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer(scaleX = -1f) // Flip horizontal

                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "DETAIL LATIHAN",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
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
                 MediaCarousel(
                     mediaItems = exercise.media
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
                     Spacer(modifier = Modifier.height(12.dp))

                     // Title
                     Text(
                         text = exercise.name.uppercase(),
                         fontSize = 28.sp,
                         fontWeight = FontWeight.Bold,
                         color = Primary
                     )

                     Spacer(modifier = Modifier.height(8.dp))

                     // Stats Row
                     StatsRow(
                         calories = exercise.cal,
                         duration = exercise.duration,
                         reps = exercise.reps
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // Description Section
                     DescriptionSection(
                         description = exercise.description.toString()
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // How to Perform Section
                     HowToPerformSection(
                         steps = exercise.howUse
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // Common Mistakes Section
                     CommonMistakesSection(
                         mistakes = exercise.commonMistakes
                     )

                     Spacer(modifier = Modifier.height(24.dp))

                     // Benefits Section
                     BenefitsSection(
                         benefits = exercise.benefits
                     )

                     Spacer(modifier = Modifier.height(120.dp)) // Space for button
                 }
             }

             // Floating Button at Bottom
             Box(
                 modifier = Modifier
                     .align(Alignment.BottomCenter)
                     .fillMaxWidth()
                     .background(Black)
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
                     onClick = {
                         navController.navigate("cameraScan/${exercise.code}")
                     },
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
                             tint = Black
                         )
                         Spacer(Modifier.width(10.dp))
                         Text(
                             "MULAI LATIHAN",
                             fontWeight = FontWeight.Bold,
                             fontSize = 16.sp,
                             color = Black
                         )
                     }
                 }
             }
         }
    }
}

// ==========================================================
//                   COMPONENT SECTION
// ==========================================================

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
    reps: String
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
            iconTint = colFire
        )

        StatItem(
            modifier = Modifier.weight(1f),
            icon = R.drawable.icoclock,
            value = duration,
            label = "MINS",
            iconTint = colHeart
        )

        StatItem(
            modifier = Modifier.weight(1f),
            icon = R.drawable.jump,
            value = reps,
            label = "REPS",
            iconTint = colLightning
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
            .clip(RoundedCornerShape(12.dp))
            .background(Card)
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
            color = White
        )

        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSub
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
            fontWeight = FontWeight.SemiBold,
            color = White
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
            color = TextSub,
            lineHeight = 20.sp
        )
    }
}


// How to Perform Section
@Composable
fun HowToPerformSection(
    steps: List<Additional>, // Pair of (title, description)
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = "Cara Melakukan")

        Spacer(modifier = Modifier.height(16.dp))

        steps.forEachIndexed { index, ( id ,title, description) ->
            StepItem(
                stepNumber = id,
                title = title,
                description = description.toString()
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
            .border(1.5.dp, Primary.copy(0.2f), shape = RoundedCornerShape(6.dp))
            .background(Primary.copy(0.1f), shape = RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Primary.copy(0.6f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = TextSub,
                lineHeight = 20.sp
            )
        }
    }
}

// Common Mistakes Section
@Composable
fun CommonMistakesSection(
    mistakes: List<Additional>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = "Kesalahan Umum")

        Spacer(modifier = Modifier.height(16.dp))

        mistakes.forEachIndexed { index, (id, title , description )->
            MistakeItem(
                title = title,
                mistake = description.toString()
            )

            if (index < mistakes.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MistakeItem(title: String, mistake: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border( 1.5.dp, colFire.copy(0.2f), shape = RoundedCornerShape(6.dp) )
            .background(
                colFire.copy(alpha = 0.1f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(28.dp)
                .background(
                    colFire.copy(0.8f),
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
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = mistake,
                fontSize = 14.sp,
                color = TextSub,
                lineHeight = 20.sp
            )
        }
    }
}

// Benefits Section
@Composable
fun BenefitsSection(
    benefits: List<Additional>, // Pair of (title, description)
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = "Manfaat")

        Spacer(modifier = Modifier.height(16.dp))

        benefits.forEachIndexed { index, (id, title , description )->
            BenefitItem(
                title = title,
                description = description.toString()
            )

            if (index < benefits.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
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
            .border( 1.5.dp, colHeart.copy(0.1f), shape = RoundedCornerShape(8.dp) )
            .background(
                colHeart.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icplay),
            contentDescription = null,
            tint = colHeart,
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .size(30.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )

    }
}
