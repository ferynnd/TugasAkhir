package dev.ferynnd.tugasakhir.data.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.data.model.HistoryExercise
import dev.ferynnd.tugasakhir.data.model.HistoryExerciseInsert
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.ui.components.DialogState
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import dev.ferynnd.tugasakhir.data.helper.parseDurationToSeconds
import dev.ferynnd.tugasakhir.data.helper.formatSecondsToReadable
import dev.ferynnd.tugasakhir.data.helper.calculateExerciseStreak

@HiltViewModel
class ExerciseViewModel @Inject constructor
    (private val supabaseClient: SupabaseClient) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var isSuccess by mutableStateOf(false)
    var textMessage by mutableStateOf("")
    var dialogState by mutableStateOf<DialogState?>(null)
        private set

    var lastInsertedId by mutableStateOf<Int?>(null)
        private set

    var historyList by mutableStateOf<List<HistoryExercise>>(emptyList())
    private set

    var totalCaloriesToday by mutableIntStateOf(0)
        private set

    var totalRepsToday by mutableIntStateOf(0)
        private set

    var totalMinutesToday by mutableIntStateOf(0)
        private set

    var totalExerciseToday by mutableIntStateOf(0)
        private set



    fun getHistoryExercise(userId: String) {
        viewModelScope.launch {

            isLoading = true
            textMessage = ""

            try {

                val result = withContext(Dispatchers.IO) {
                    supabaseClient.postgrest
                        .from("history_exercise")
                        .select {
                            filter {
                                eq("user_id", userId)
                            }
                            order("created_at", Order.DESCENDING)
                        }
                        .decodeList<HistoryExercise>()
                }

                // =============================
                // PARSING DI VIEWMODEL
                // =============================

                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date())

                val todayHistory = result.filter {
                    it.createdAt?.startsWith(today) == true
                }

                totalCaloriesToday = todayHistory.sumOf {
                    it.totalCalorie ?: 0
                }

                totalRepsToday = todayHistory.sumOf {
                    it.reps ?: 0
                }

                totalMinutesToday = todayHistory.sumOf {

                    val parts = it.duration?.split(":") ?: return@sumOf 0

                    val hours = parts.getOrNull(0)?.toIntOrNull() ?: 0
                    val minutes = parts.getOrNull(1)?.toIntOrNull() ?: 0

                    (hours * 60) + minutes
                }

                historyList = result
                isSuccess = true

            } catch (e: Exception) {

                textMessage = e.message ?: "Gagal mengambil history"
                Log.e("SUPABASE", "Error getHistory", e)

            } finally {
                isLoading = false
            }
        }
    }


    fun storeHistoryExercise(
        userId: String,
        reps: Int,
        durationSeconds: Int,
        weightKg: Int,
        exerciseCode: ExerciseCode,
    ) {
        viewModelScope.launch {

            isLoading = true
            isSuccess = false
            textMessage = ""
            lastInsertedId = null

            try {

                val durationMinutes = durationSeconds / 60.0
                val durationHours   = durationMinutes / 60.0


                val met = when (exerciseCode) {
                    ExerciseCode.PUSH_UP -> 8.0
                    ExerciseCode.SQUAT   -> 8.0
                    ExerciseCode.SIT_UP  -> 8.0
                }
                val totalCalorie = (met * weightKg * durationHours).toInt()

                val hours   = durationSeconds / 3600
                val minutes = (durationSeconds % 3600) / 60
                val seconds = durationSeconds % 60
                val durationString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                // 🔥 PINDAH KE IO THREAD YANG BENAR
                val insertedData = withContext(Dispatchers.IO) {
                    val data = HistoryExerciseInsert(
                        userId          = userId,
                        codeExercise    = exerciseCode.name,
                        reps            = reps,
                        duration        = durationString,   // ✅ format HH:MM:SS
                        totalCalorie    = totalCalorie,
                    )

                    supabaseClient.postgrest
                        .from("history_exercise")
                        .insert(data) { select() }
                        .decodeSingle<HistoryExercise>()

                }

                lastInsertedId = insertedData.id
                isSuccess = true
                textMessage = "History berhasil disimpan"

            } catch (e: Exception) {
                textMessage = e.message ?: "Terjadi kesalahan"
                Log.e("SUPABASE", "Insert gagal", e)
            } finally {
                isLoading = false
            }
        }
    }


    // Di ExerciseViewModel.kt
    var currentHistory by mutableStateOf<HistoryExercise?>(null)
    var lastHistory by mutableStateOf<HistoryExercise?>(null)

    fun getSummaryData(historyId: Int) {
        viewModelScope.launch {
            try {
                // 1. Ambil sesi saat ini (Current Session)
                val current = supabaseClient.postgrest.from("history_exercise")
                    .select {
                        filter {
                            eq("id", historyId)
                        }
                    }.decodeSingleOrNull<HistoryExercise>()

                currentHistory = current

                // 2. Ambil sesi sebelumnya (Last Session) dengan kode latihan yang sama
                if (current != null) {
                    val last = supabaseClient.postgrest.from("history_exercise")
                        .select {
                            filter {
                                eq("user_id", current.userId ?: "")
                                eq("code_exercise", current.codeExercise ?: "")
                                lt("id", historyId) // Cari ID sebelum ID saat ini
                            }
                            // Urutkan berdasarkan ID secara descending (terbaru dari yang lama)
                            order(column = "id", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                            limit(1)
                        }.decodeList<HistoryExercise>()
                        .firstOrNull()

                    lastHistory = last
                }

                Log.d("SummaryData", "Current: $currentHistory, Last: $lastHistory")
            } catch (e: Exception) {
                Log.e("SummaryError", "Gagal mengambil data: ${e.message}")
            }
        }
    }
    var totalCalories by mutableStateOf(0)
    var totalExercises by mutableStateOf(0)
    var totalDurationFormatted by mutableStateOf("0j 0m")
    var currentStreak by mutableStateOf(0)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHistoryProfile(userId: String) {
        viewModelScope.launch {
            try {
                isLoading = true

                // 1. Ambil semua data history user
                val result = supabaseClient.postgrest.from("history_exercise")
                    .select {
                        filter {
                            eq("user_id", userId)
                        }
                        order("created_at", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                    }.decodeList<HistoryExercise>()

                historyList = result

                // 2. Agregasi Data
                if (result.isNotEmpty()) {
                    // Total Latihan
                    totalExercises = result.size

                    // Total Kalori
                    totalCalories = result.sumOf { it.totalCalorie ?: 0 }

                    // Total Durasi (Konversi dari HH:mm:ss ke total detik lalu diformat)
                    val totalSeconds = result.sumOf { parseDurationToSeconds(it.duration ?: "00:00:00") }
                    totalDurationFormatted = formatSecondsToReadable(totalSeconds)

                    // 3. Hitung Streak Latihan
                    currentStreak = calculateExerciseStreak(result)
                }

            } catch (e: Exception) {
                Log.e("ProfileError", "Gagal load profil: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


    fun getHistoryHome(userId: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                val result = supabaseClient.postgrest.from("history_exercise")
                    .select {
                        filter {
                            eq("user_id", userId)
                        }
                        order("created_at", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                    }.decodeList<HistoryExercise>()
                historyList = result

                if(result.isNotEmpty()) {

                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(Date())

                    val todayHistory = result.filter {
                        it.createdAt?.startsWith(today) == true
                    }

                    totalCaloriesToday = todayHistory.sumOf {
                        it.totalCalorie ?: 0
                    }

                    totalExerciseToday = todayHistory.size

                }
            } catch (e: Exception) {
                Log.e("HomeError", "Gagal load profil: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

}