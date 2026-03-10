package dev.ferynnd.tugasakhir.data.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
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
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor
    (private val supabaseClient: SupabaseClient) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var isSuccess by mutableStateOf(false)
    var textMessage by mutableStateOf("")
    var dialogState by mutableStateOf<DialogState?>(null)
        private set

    var historyList by mutableStateOf<List<HistoryExercise>>(emptyList())
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
                withContext(Dispatchers.IO) {
                    val data = HistoryExerciseInsert(
                        userId          = userId,
                        codeExercise    = exerciseCode.name,
                        reps            = reps,
                        duration        = durationString,   // ✅ format HH:MM:SS
                        totalCalorie    = totalCalorie,
                    )

                    val response = supabaseClient.postgrest
                        .from("history_exercise")
                        .insert(data) { select() }
                        .decodeSingle<HistoryExercise>()

                    Log.d("SUPABASE", "Inserted: $response")
                }

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

}