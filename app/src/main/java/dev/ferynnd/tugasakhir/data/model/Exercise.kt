package dev.ferynnd.tugasakhir.data.model

import kotlin.time.Duration

data class Exercise(
    val id: Int,
    val name: String,
    val description: String,
    val met: Float,
    val reps: Int,
    val sets: Int,
    val duration: Duration? = null,
    val type: ExerciseType,
)


data class ExerciseResult(
    val counter: Int,
    val status: Boolean
)


enum class ExerciseType {
    PUSH_UP,
    SQUAT,
    SIT_UP,
}


val dummyExercises = listOf(
    Exercise(
        id = 1,
        name = "Push Up",
        description = "Latihan untuk memperkuat otot dada, bahu, dan lengan.",
        met = 8.0f,
        reps = 12,
        sets = 3,
        type = ExerciseType.PUSH_UP
    ),

    Exercise(
        id = 2,
        name = "Squat",
        description = "Melatih otot paha, pinggul, dan gluteus.",
        met = 5.0f,
        reps = 15,
        sets = 3,
        type = ExerciseType.SQUAT
    ),

    Exercise(
        id = 3,
        name = "Sit Up",
        description = "Latihan inti untuk memperkuat otot perut.",
        met = 4.0f,
        reps = 20,
        sets = 3,
        type = ExerciseType.SIT_UP
    ),
)
