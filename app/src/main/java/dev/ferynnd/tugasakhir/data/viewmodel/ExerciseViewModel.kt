package dev.ferynnd.tugasakhir.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.data.model.HistoryExercise
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.ui.components.DialogState
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(private val supabaseClient: SupabaseClient) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var isSuccess by mutableStateOf(false)
    var textMessage by mutableStateOf("")
    var dialogState by mutableStateOf<DialogState?>(null)
        private set

    fun getHistoryExercise() {

    }

    fun storeHistoryExercise(
        userId: String,
        reps: Int,
        durationMinutes: Int,
        weightKg: Double,
        exerciseCode: ExerciseCode,
        listFormCorrect: List<Double>
    ) {
        viewModelScope.launch {

            isLoading = true
            isSuccess = false
            textMessage = ""

            try {

                // ======================
                // 1. Tentukan MET
                // ======================

                val met = when (exerciseCode) {
                    ExerciseCode.PUSH_UP -> 8.0
                    ExerciseCode.SQUAT -> 5.0
                    ExerciseCode.SIT_UP -> 6.0
                }

                // ======================
                // 2. Hitung Kalori
                // ======================

                val totalCalorie =
                    ((met * 3.5 * weightKg) / 200) * durationMinutes

                // ======================
                // 3. Hitung Form Correct
                // ======================

                val formCorrect =
                    if (listFormCorrect.isNotEmpty())
                        listFormCorrect.average()
                    else 0.0

                // ======================
                // 4. Buat Object
                // ======================

                val history = HistoryExercise(
                    id = 0,
                    userId = userId,
                    codeExercise = exerciseCode.name,
                    reps = reps,
                    duration = "00:${String.format("%02d", durationMinutes)}:00",
                    totalCalorie = totalCalorie.toInt(),
                    formCorrect = formCorrect,
                    createdAt = ""
                )

                Dispatchers.IO.apply {
                    supabaseClient.client.postgrest
                        .from("history_exercise")
                        .insert(history)
                }

                isSuccess = true
                textMessage = "History berhasil disimpan"

            } catch (e: Exception) {
                textMessage = e.message ?: "Terjadi kesalahan"
            } finally {
                isLoading = false
            }
        }
    }


}