package dev.ferynnd.tugasakhir.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

data class Exercise(
    val id: Int,
    val code : String,
    val name: String,
    val met: Float,
    val description: String? = null,
)

enum class ExerciseCode {
    PUSH_UP, SIT_UP, SQUAT
}

@Serializable
data class HistoryExercise(
    val id: Int,
    @SerialName("user_id")
    val userId: String,
    @SerialName("exercise_id")
    val exerciseId: Int,
    val reps: Int?,
    val duration: String?,     // "00:15:00"
    @SerialName("total_calorie")
    val totalCalorie: Int,
    @SerialName("created_at")
    val createdAt: String
)

data class ExerciseResult(
    val counter: Int,
    val status: Boolean
)


val dummyExercises = listOf(
    Exercise(
        id = 1,
        code = ExerciseCode.PUSH_UP.name,
        name = "Push Up",
        met = 8.0f
    ),
    Exercise(
        id = 2,
        code = ExerciseCode.SIT_UP.name,
        name = "Sit Up",
        met = 6.0f
    ),
    Exercise(
        id = 3,
        code = ExerciseCode.SQUAT.name,
        name = "Squat",
        met = 7.0f
    )
)
