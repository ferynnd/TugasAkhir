package dev.ferynnd.tugasakhir.data.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import dev.ferynnd.tugasakhir.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ferynnd.tugasakhir.data.model.CategoryBmi
import dev.ferynnd.tugasakhir.data.model.Gender
import dev.ferynnd.tugasakhir.data.model.Profile
import dev.ferynnd.tugasakhir.data.model.UserBmi
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.ui.components.DialogState
import dev.ferynnd.tugasakhir.ui.theme.colError
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel
    @Inject constructor(
        private val supabaseClient: SupabaseClient
    ) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var isSuccess by mutableStateOf(false)
    var textMessage by mutableStateOf("")
    var dialogState by mutableStateOf<DialogState?>(null)
        private set
    // State untuk menyimpan data profile
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var avatar by mutableStateOf("")

    var showBmiWarning by mutableStateOf(false)
    private var hasCheckBMIWarning = false
    private var hasLoadedProfile = false
    // State untuk menyimpan data bmi
    var heightValue by mutableStateOf("0")
    var weightValue by mutableIntStateOf(60)
    var ageValue by mutableStateOf("0")
    var selectedGender by mutableStateOf(Gender.MALE)
    var bmiResult by mutableDoubleStateOf(0.0)
    var bmiCategory by mutableStateOf("NORMAL")



    fun showError(title: String, message: String) {
        dialogState = DialogState(
            lottieRes = R.raw.error,
            colorBg = colError.copy(alpha = 0.15f),
            title = title,
            message = message,
            autoDismiss = true
        )
    }

    fun dismissDialog() {
        dialogState = null
    }

    fun resetSuccess() {
        isSuccess = false
    }

    fun clearMessage() {
        textMessage = ""
    }

    fun loadProfileIfNeeded(userId: String) {
        if (hasLoadedProfile) return
        hasLoadedProfile = true
        getProfile(userId)
        getUserBMI(userId)
    }


     fun getProfile(userId: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                val profile = supabaseClient.postgrest
                    .from("profiles")
                    .select { filter { eq("id", userId) } }
                    .decodeSingle<Profile>()

                fullName = profile.fullName ?: ""
                email = profile.email ?: ""
                avatar = profile.avatar ?: ""
                isLoading = false
            } catch (e: Exception) {
                Log.e("HomeVM", "Error Get: ${e.message}")
                showError("Error", "Gagal mengambil data: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateProfile(userId: String, name: String, imageUri: String?, context: Context) {
        viewModelScope.launch {
            try {
                isLoading = true
                isSuccess = false
                textMessage = ""
                var finalImageUrl = imageUri // default pakai yang lama

                // Jika imageUri adalah file lokal (dimulai dengan content://)
                if (imageUri?.startsWith("content://") == true) {
                    val inputStream = context.contentResolver.openInputStream(Uri.parse(imageUri))
                    inputStream?.use { stream -> // Menggunakan .use agar otomatis ditutup
                        val bytes = stream.readBytes()
                        val fileName = "$userId/avatar.jpg"
                        val bucket = supabaseClient.storage.from("avatars")

                        bucket.upload(fileName, bytes) {
                            upsert = true
                        }
                        finalImageUrl = bucket.publicUrl(fileName)
                    }
                }

                supabaseClient.postgrest.from("profiles").update(
                    {
                        set("full_name", name)
                        set("avatar_url", finalImageUrl)
                    }
                ) {
                    filter {
                        eq("id", userId)
                    }
                }

                getProfile(userId)
                isSuccess = true
                textMessage = "Berhasil memperbarui profile."
            } catch (e: Exception) {
                showError("Error", "Gagal memperbarui profil")
                isSuccess = false
            } finally {
                isLoading = false
            }
        }
    }

    fun getUserBMI(userId: String) {
        if (hasCheckBMIWarning) return
        viewModelScope.launch {
            try {
                isLoading = true
                val response = supabaseClient.postgrest
                    .from("user_bmi")
                    .select {
                        filter {
                            eq("user_id", userId)
                        }
                    }
                    .decodeSingleOrNull<UserBmi>() // Gunakan OrNull agar tidak crash jika data kosong

                if (response != null) {
                    showBmiWarning = false
                    bmiResult = response.bmiValue
                    bmiCategory = response.categoryValue.name
                    heightValue = response.height.toString()
                    weightValue = response.weight
                    ageValue = response.age.toString()
                    selectedGender = response.gender
                } else {
                    showBmiWarning = true
                    showError("Error", "Data BMI tidak ditemukan")
                }
                hasCheckBMIWarning = true
            } catch (e: Exception) {
                showError("Error", "Gagal mengambil data: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateBMI(userId: String, gender : Gender, height : Int, weight : Int, age : Int) {
        try {
            isLoading = true
            isSuccess = false
            textMessage = ""

            val heightInMeter = height / 100.0
            val bmiResult = weight / (heightInMeter * heightInMeter)
            val category = determineCategory(bmiResult)

            val data = UserBmi(
                userId = userId,
                gender = gender,
                height = height,
                weight = weight,
                age = age,
                bmiValue = bmiResult,
                categoryValue = category
            )

            viewModelScope.launch {
                try {
                    // OnConflict menentukan kolom mana yang jadi acuan (biasanya user_id)
                    supabaseClient.postgrest["user_bmi"].upsert(data) {
                        onConflict = "user_id"
                    }
                    isSuccess = true
                    textMessage = "Berhasil memperbarui BMI."
                    hasCheckBMIWarning = false
                    getUserBMI(userId)
                } catch (e: Exception) {
                    showError("Error", "Gagal memperbarui BMI")
                    isSuccess = false
                } finally {
                    isLoading = false
                }
            }
        } catch ( e: Exception) {
            showError("Error", "Gagal memperbarui BMI")
            isSuccess = false
        }
    }

    private fun determineCategory(bmi: Double): CategoryBmi {
        return when {
            bmi < 18.5 -> CategoryBmi.UNDERWEIGHT
            bmi < 25.0 -> CategoryBmi.NORMAL
            bmi < 30.0 -> CategoryBmi.OVERWEIGHT
            else -> CategoryBmi.OBESE
        }
    }

}