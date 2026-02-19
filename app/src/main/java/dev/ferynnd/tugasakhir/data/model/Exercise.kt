package dev.ferynnd.tugasakhir.data.model

import androidx.compose.ui.graphics.Color
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colEmail
import dev.ferynnd.tugasakhir.ui.theme.colWarning
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Exercise(
    val id: Int,
    val code : String,
    val name: String,
    val cal: String,
    val duration : String,
    val reps : String,
    val met: Float,
    val description: String? = null,
    val howUse : List<Additional>,
    val commonMistakes : List<Additional>,
    val benefits : List<Additional>,
    val color: Color = White,
    val icon: Int = R.drawable.icpushup,
    val media : List<MediaComp>
)

data class Additional(
    val id: Int,
    val title: String,
    val description: String? = null
)

enum class ExerciseCode {
    PUSH_UP, SIT_UP, SQUAT
}

@Serializable
data class HistoryExercise(
    val id: Int,
    @SerialName("user_id")
    val userId: String,
    @SerialName("code_exercise")
    val codeExercise: String,
    val reps: Int?,
    val duration: String?,     // "00:15:00"
    @SerialName("total_calorie")
    val totalCalorie: Int,
    @SerialName("form_correct")
    val formCorrect: Double,
    @SerialName("created_at")
    val createdAt: String
)

sealed class MediaComp {
    data class Image(val url: String) : MediaComp()
    data class Video(val url: String) : MediaComp()
}


val dummyExercises = listOf(

    Exercise(
        id = 1,
        code = ExerciseCode.PUSH_UP.name,
        name = "Push Up",
        cal = "8–10",
        duration = "10–15 ",
        reps = "3 x 12",
        met = 8.0f,
        description = "Latihan untuk memperkuat otot dada, bahu, dan trisep.",
        howUse = listOf(
            Additional(1, "Posisi Awal", "Letakkan tangan sejajar bahu dan tubuh lurus."),
            Additional(2, "Turunkan Badan", "Turunkan tubuh hingga siku 90 derajat."),
            Additional(3, "Dorong Kembali", "Dorong tubuh kembali ke posisi awal.")
        ),
        commonMistakes = listOf(
            Additional(1, "Pinggul Turun", "Pinggul terlalu turun menyebabkan cedera punggung."),
            Additional(2, "Siku Terlalu Melebar", "Dapat memberi tekanan berlebih pada bahu.")
        ),
        benefits = listOf(
            Additional(1, "Menguatkan Dada"),
            Additional(2, "Melatih Bahu"),
            Additional(3, "Meningkatkan Stabilitas Core")
        ),
        color = Primary,
        icon = R.drawable.icpushup,
        media = listOf(
            MediaComp.Image("https://picsum.photos/800/400"),
            MediaComp.Video("https://media.w3.org/2010/05/sintel/trailer.mp4"),
            MediaComp.Image("https://picsum.photos/800/401")
        )
    ),

    Exercise(
        id = 2,
        code = ExerciseCode.SIT_UP.name,
        name = "Sit Up",
        cal = "6–8",
        duration = "10–15",
        reps = "3 x 15",
        met = 6.0f,
        description = "Latihan untuk memperkuat otot perut dan core.",
        howUse = listOf(
            Additional(1, "Posisi Awal", "Berbaring dengan lutut ditekuk."),
            Additional(2, "Angkat Tubuh", "Angkat tubuh hingga mendekati lutut."),
            Additional(3, "Turun Perlahan", "Turunkan tubuh secara perlahan.")
        ),
        commonMistakes = listOf(
            Additional(1, "Menarik Leher", "Dapat menyebabkan cedera leher."),
            Additional(2, "Gerakan Terlalu Cepat", "Mengurangi efektivitas latihan.")
        ),
        benefits = listOf(
            Additional(1, "Menguatkan Core"),
            Additional(2, "Meningkatkan Stabilitas"),
            Additional(3, "Membantu Postur Tubuh")
        ),
        color = colEmail,
        icon = R.drawable.icsitup,
        media = listOf(
            MediaComp.Image("https://picsum.photos/800/400"),
            MediaComp.Video("https://media.w3.org/2010/05/sintel/trailer.mp4"),
            MediaComp.Image("https://picsum.photos/800/401")
        )
    ),

    Exercise(
        id = 3,
        code = ExerciseCode.SQUAT.name,
        name = "Squat",
        cal = "7–9",
        duration = "10–15",
        reps = "3 x 12",
        met = 7.0f,
        description = "Latihan untuk memperkuat otot kaki dan glutes.",
        howUse = listOf(
            Additional(1, "Posisi Berdiri", "Berdiri dengan kaki selebar bahu."),
            Additional(2, "Turunkan Pinggul", "Turunkan hingga paha sejajar lantai."),
            Additional(3, "Kembali Berdiri", "Dorong tubuh kembali ke posisi awal.")
        ),
        commonMistakes = listOf(
            Additional(1, "Lutut Melewati Jari Kaki", "Dapat memberi tekanan berlebih."),
            Additional(2, "Punggung Membungkuk", "Risiko cedera punggung.")
        ),
        benefits = listOf(
            Additional(1, "Menguatkan Otot Kaki"),
            Additional(2, "Meningkatkan Keseimbangan"),
            Additional(3, "Membakar Kalori Lebih Efektif")
        ),
        color = colWarning,
        icon = R.drawable.icsquat,
        media = listOf(
            MediaComp.Image("https://picsum.photos/800/400"),
            MediaComp.Video("https://media.w3.org/2010/05/sintel/trailer.mp4"),
            MediaComp.Image("https://picsum.photos/800/401")
        )
    )
)
