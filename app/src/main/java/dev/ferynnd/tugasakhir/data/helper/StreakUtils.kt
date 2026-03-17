package dev.ferynnd.tugasakhir.data.helper

import android.os.Build
import androidx.annotation.RequiresApi
import dev.ferynnd.tugasakhir.data.model.HistoryExercise

// Mengubah "00:15:30" menjadi total detik (Integer)
fun parseDurationToSeconds(duration: String): Int {
    val parts = duration.split(":")
    if (parts.size < 3) return 0
    return try {
        val h = parts[0].toInt() * 3600
        val m = parts[1].toInt() * 60
        val s = parts[2].toInt()
        h + m + s
    } catch (e: Exception) { 0 }
}

// Mengubah detik menjadi format "Xj Ym"
fun formatSecondsToReadable(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    return if (hours > 0) "${hours} Jam ${minutes} Min" else "${minutes} Min"
}

// Logika menghitung streak hari berturut-turut
@RequiresApi(Build.VERSION_CODES.O)
fun calculateExerciseStreak(history: List<HistoryExercise>): Int {
    if (history.isEmpty()) return 0

    // Ambil tanggal unik dari 'created_at', urutkan dari yang terbaru
    val dates = history.mapNotNull { it.createdAt?.substring(0, 10) }
        .distinct()
        .map { java.time.LocalDate.parse(it) }
        .sortedDescending()

    var streak = 0
    var today = java.time.LocalDate.now()

    // Cek apakah latihan terakhir adalah hari ini atau kemarin
    if (dates.first() != today && dates.first() != today.minusDays(1)) {
        return 0
    }

    var currentDate = dates.first()
    streak = 1

    for (i in 1 until dates.size) {
        if (dates[i] == currentDate.minusDays(1)) {
            streak++
            currentDate = dates[i]
        } else if (dates[i] == currentDate) {
            continue // Lewati jika ada dua latihan di hari yang sama
        } else {
            break // Streak putus
        }
    }
    return streak
}