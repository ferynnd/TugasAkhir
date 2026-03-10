package dev.ferynnd.tugasakhir.ui.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.data.model.dummyExercises
import dev.ferynnd.tugasakhir.R


@Composable
fun PoseIllustration(
    exerciseCode: ExerciseCode,
    modifier: Modifier = Modifier
) {

    val exercise = dummyExercises.find { it.code == exerciseCode.name }

    val imageRes = exercise?.icon ?: R.drawable.icpushup

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = exercise?.name,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}
