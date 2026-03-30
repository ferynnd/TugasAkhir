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
data class HistoryExerciseInsert(
    @SerialName("user_id")
    val userId: String,
    @SerialName("code_exercise")
    val codeExercise: String,
    val reps: Int,
    val duration: String,
    @SerialName("total_calorie")
    val totalCalorie: Int,
)

@Serializable
data class HistoryExercise(
    val id: Int,
    @SerialName("user_id")
    val userId: String?,
    @SerialName("code_exercise")
    val codeExercise: String?,
    val reps: Int?,
    val duration: String?, // interval -> "00:15:00"
    @SerialName("total_calorie")
    val totalCalorie: Int?,
    @SerialName("created_at")
    val createdAt: String?   // ISO STRING dari Supabase
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
        cal = "80–120",
        duration = "10–15",
        reps = "3 x 10",
        met = 8.0f,
        description = "Latihan beban tubuh yang menguji kekuatan dan kontrol tubuh bagian atas, kemampuan menjaga inti tubuh tetap kencang, serta kemampuan menghasilkan ketegangan otot di seluruh tubuh secara keseluruhan.",
        howUse = listOf(
            Additional(1, "Posisi Awal", "Letakkan tangan di lantai dengan jarak selebar bahu, regangkan kaki ke belakang, dan berdirilah dengan ujung jari kaki."),
            Additional(2, "Jaga Tubuh Lurus", "Bahu, pinggul, lutut, dan kaki harus membentuk garis lurus."),
            Additional(3, "Turunkan Badan", "Tetap menatap ke depan dengan sudut sekitar 45 derajat, tekuk siku, dan mulai turunkan tubuh ke tanah. Biarkan siku sedikit melebar dari tubuh."),
            Additional(4, "Posisi Bawah", "Berhenti saat dada menyentuh tanah. Pada titik ini, hanya dada dan jari kaki yang menyentuh lantai."),
            Additional(5, "Dorong Kembali", "Berhenti sejenak, lalu dorong ke bawah dengan keras dan luruskan lengan hingga kembali ke posisi awal.")
        ),
        commonMistakes = listOf(
            Additional(1, "Dada Tidak Menyentuh Lantai", "Rentang gerak tidak penuh sehingga mengurangi efektivitas latihan."),
            Additional(2, "Punggung Membungkuk", "Menyebabkan tekanan berlebih pada punggung bawah dan risiko cedera."),
            Additional(3, "Siku Terlalu Melebar", "Tangan diletakkan lebih lebar dari bahu sehingga siku melebar berlebihan dan memberi tekanan pada bahu."),
            Additional(4, "Kepala Tidak Sejajar Tulang Belakang", "Kepala condong ke depan dan tidak sejajar dengan tulang belakang."),
            Additional(5, "Pinggul Terlalu Tinggi", "Pinggul terlalu tinggi saat dada menyentuh lantai."),
            Additional(6, "Pinggul Turun", "Pinggul turun ke lantai setelah dada menyentuh lantai.")
        ),
        benefits = listOf(
            Additional(1, "Menguatkan Otot Dada"),
            Additional(2, "Melatih Bahu dan Trisep"),
            Additional(3, "Meningkatkan Stabilitas Core"),
            Additional(4, "Melatih Ketegangan Otot Seluruh Tubuh")
        ),
        color = Primary,
        icon = R.drawable.icpushup,
        media = listOf(
            MediaComp.Image("https://raw.githubusercontent.com/ferynnd/PublicAsset/refs/heads/main/pushup.jpeg")
        )
    ),
    Exercise(
        id = 2,
        code = ExerciseCode.SIT_UP.name,
        name = "Sit Up",
        cal = "80–120",
        duration = "10–15",
        reps = "3 x 20",
        met = 8.0f,
        description = "Latihan variasi dari crunch yang menggunakan otot inti dan fleksor pinggul untuk mengangkat tubuh bagian atas dari tanah.",
        howUse = listOf(
            Additional(1, "Posisi Awal", "Berbaring telentang dan posisikan lutut pada sudut 90 derajat. Kaitkan kaki di bawah benda yang kokoh atau minta rekan memegang pergelangan kaki."),
            Additional(2, "Posisi Tangan", "Posisikan ujung jari di pelipis dan letakkan lengan ke belakang sehingga siku menyentuh tanah."),
            Additional(3, "Angkat Tubuh", "Kontraksikan otot inti dengan kuat dan angkat tubuh sejauh mungkin. Bawa siku ke depan hingga menyentuh atau melewati lutut."),
            Additional(4, "Turun Perlahan", "Turunkan tubuh kembali ke posisi awal. Ini dihitung sebagai satu repetisi.")
        ),
        commonMistakes = listOf(
            Additional(1, "Menarik Leher", "Menggunakan tangan untuk menarik kepala ke depan dapat menyebabkan cedera leher."),
            Additional(2, "Rentang Gerak Tidak Penuh", "Tidak mengangkat tubuh cukup tinggi hingga siku menyentuh atau melewati lutut mengurangi efektivitas latihan."),
            Additional(3, "Gerakan Terlalu Cepat", "Bergerak terlalu cepat tanpa kontrol mengurangi aktivasi otot inti.")
        ),
        benefits = listOf(
            Additional(1, "Menguatkan Otot Inti"),
            Additional(2, "Melatih Fleksor Pinggul"),
            Additional(3, "Meningkatkan Stabilitas Tubuh"),
            Additional(4, "Membantu Memperbaiki Postur Tubuh")
        ),
        color = colEmail,
        icon = R.drawable.icsitup,
        media = listOf(
            MediaComp.Image("https://raw.githubusercontent.com/ferynnd/PublicAsset/refs/heads/main/situp.jpeg")
        )
    ),
    Exercise(
        id = 3,
        code = ExerciseCode.SQUAT.name,
        name = "Squat",
        cal = "80–120",
        duration = "10–15",
        reps = "3 x 10",
        met = 8.0f,
        description = "Salah satu gerakan manusia yang paling mendasar yang melibatkan hip drive — ekstensi lutut dan ekstensi pinggul — untuk melatih quadriceps, glutes, hamstrings, dan adductors.",
        howUse = listOf(
            Additional(1, "Posisi Awal", "Berdiri dengan tumit selebar bahu, jari kaki mengarah ke luar sekitar 30 derajat, dan tangan dilipat di dada atau diulurkan ke depan."),
            Additional(2, "Turunkan Tubuh", "Bengkokkan lutut sambil menjaga punggung tetap lurus dan mata melihat ke depan. Dorong pinggul ke belakang dan ke bawah secara bersamaan."),
            Additional(3, "Dorong Lutut ke Luar", "Saat turun, dorong lutut ke luar dan biarkan pinggul turun di antara celah lutut. Pastikan kaki tetap datar tanpa bagian telapak kaki terangkat dari lantai."),
            Additional(4, "Kedalaman Penuh", "Terus turun hingga sendi pinggul sejajar dengan sendi lutut."),
            Additional(5, "Kembali Berdiri", "Dorong pinggul ke atas hingga kembali ke posisi awal. Ini dihitung sebagai satu repetisi.")
        ),
        commonMistakes = listOf(
            Additional(1, "Punggung Membungkuk", "Hilangnya posisi tulang belakang netral meningkatkan risiko cedera punggung."),
            Additional(2, "Tumit Terangkat", "Perpindahan berat badan ke ujung jari kaki menyebabkan hilangnya kontak antara tumit dan tanah."),
            Additional(3, "Kedalaman Kurang", "Squat yang tidak cukup dalam sehingga sendi pinggul tidak sejajar dengan sendi lutut."),
            Additional(4, "Lutut Mengarah ke Dalam", "Gerakan lateral lutut yang tidak tepat memberi tekanan berlebih pada sendi lutut."),
            Additional(5, "Pinggul Terlalu Jauh ke Belakang", "Pinggul didorong terlalu jauh ke belakang sehingga dada condong ke depan."),
            Additional(6, "Pinggul Tertahan", "Pinggul didorong terlalu ke depan sehingga pinggul tertahan."),
            Additional(7, "Kepala Miring ke Bawah", "Kepala tidak sejajar dengan tulang belakang."),
            Additional(8, "Posisi Kaki Terlalu Lebar", "Jarak kaki melebihi bahu sehingga mengganggu mekanika squat.")
        ),
        benefits = listOf(
            Additional(1, "Menguatkan Quadriceps"),
            Additional(2, "Melatih Glutes dan Hamstrings"),
            Additional(3, "Meningkatkan Keseimbangan dan Stabilitas"),
            Additional(4, "Membakar Kalori Lebih Efektif")
        ),
        color = colWarning,
        icon = R.drawable.icsquat,
        media = listOf(
            MediaComp.Image("https://raw.githubusercontent.com/ferynnd/PublicAsset/refs/heads/main/squat.jpeg")
        )
    )
)