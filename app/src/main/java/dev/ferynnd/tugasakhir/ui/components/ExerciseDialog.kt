package dev.ferynnd.tugasakhir.ui.components

import android.icu.text.CaseMap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.data.model.Exercise
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Input
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colWarning

@Composable
fun ExerciseListDialog(
    exercises: List<Exercise>,
    onDismiss: () -> Unit,
    onSelect: (Exercise) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        shape = RoundedCornerShape(20.dp),
        containerColor = White,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pilih Latihan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Black,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                exercises.forEach { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        onClick = { onSelect(exercise) }
                    )
                }
            }
        }
    )
}


@Composable
fun ExerciseItem(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Background.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ICON BOX
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = exercise.color.copy(0.15f)// Tambahkan property color di model
                )
            ) {
                CustomIcon(
                    iconRes = exercise.icon,
                    tint = exercise.color,
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp)
                        .padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CustomIcon(
                        iconRes = R.drawable.icfire,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier
                            .size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = exercise.cal + " Kal ",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            // Arrow
            Text(
                text = "›",
                fontSize = 22.sp,
                color = Color.Gray
            )
        }
    }
}
