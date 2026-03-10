package dev.ferynnd.tugasakhir.data.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import dev.ferynnd.tugasakhir.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.ui.components.DialogState
import dev.ferynnd.tugasakhir.ui.theme.colError
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor (
    private val supabaseClient: SupabaseClient
): ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var isSuccess by mutableStateOf(false)
    var textMessage by mutableStateOf("")
    var dialogState by mutableStateOf<DialogState?>(null)
        private set
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var avatar by mutableStateOf("")
    var isUpdateSuccess by mutableStateOf(false)

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


    fun validateInputRegister(
        email: String,
        pass: String,
        confirmPass: String,
        fullname: String,
        isChecked: Boolean
    ) {
        when {
            fullname.isBlank() ->
                showError("Error", "Nama lengkap tidak boleh kosong")

            email.isBlank() ->
                showError("Error", "Email tidak boleh kosong")

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                showError("Error", "Email tidak valid")

            pass.length < 6 ->
                showError("Error", "Password minimal 6 karakter")

            pass != confirmPass ->
                showError("Error", "Password tidak sama")

            !isChecked ->
                showError("Error", "Harus menyetujui syarat dan ketentuan")

            else -> {
                onRegister(email, pass, fullname)
                Log.d("AuthViewModel", "Validasi berhasil")
            }
        }
    }


    private fun onRegister(email: String, pass: String, username: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                supabaseClient.auth.signUpWith(Email) {
                    this.email = email
                    this.password = pass
                    data = buildJsonObject {
                         put("full_name", JsonPrimitive(username))
                    }
                }
                isSuccess = true
                Log.d("AuthViewModel", "Registrasi berhasil")
            } catch (e: Exception) {
                showError("Error", e.localizedMessage ?: "Registrasi Gagal")
                Log.d("AuthViewModel", "Registrasi gagal: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }

     fun validateInputLogin(email: String, password: String) {
        Log.d("AuthViewModel", "Validasi dimulai")

        when {
            email.isBlank() || password.isBlank() -> {
                showError("Error", "Email dan password tidak boleh kosong")
                return
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showError("Error", "Email tidak valid")
                return
            }
        }

        Log.d("AuthViewModel", "Validasi berhasil")
        onLogin(email, password)
    }


    private fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            Log.d("AuthViewModel", "Login dimulai")

            try {
                supabaseClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                isSuccess = true
                Log.d("AuthViewModel", "Login berhasil")
            } catch (e: Exception) {
                val message = when {
                    e.message?.contains("Invalid login credentials", true) == true ->
                        "Email atau password salah"
                    e.message?.contains("Email not confirmed", true) == true ->
                        "Email belum diverifikasi"
                    else ->
                        "Login gagal, silakan coba lagi"
                }

                showError("Error", message)
                isSuccess = false
            } finally {
                isLoading = false
            }
        }
    }

    fun onGoogleLoginSuccess() {
        isSuccess = true
        Log.d("GOOGLE", "isSucces = ${isSuccess}")
    }

     fun logout() {
        viewModelScope.launch {
            supabaseClient.auth.signOut() // logout Supabase
        }
    }

     fun isUserLoggedIn(): Boolean {
        return supabaseClient.auth.currentSessionOrNull() != null
    }

    fun changePassword(email: String, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                isSuccess = false
                textMessage = ""
                Log.d("AuthViewModel", "Email : ${email} dan Password : ${oldPassword}")
                supabaseClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = oldPassword
                }
                // 2. EKSEKUSI: Jika login berhasil, baru update password
                supabaseClient.auth.updateUser {
                    password = newPassword
                }
                isSuccess = true
                textMessage = "Berhasil memperbarui password."
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("invalid", ignoreCase = true) == true -> "Password lama tidak valid."
                    e.message?.contains("network", ignoreCase = true) == true -> "Koneksi internet bermasalah."
                    e.message?.contains("weak", ignoreCase = true) == true -> "Password terlalu lemah. gunakan kombinasi huruf besar, huruf kecil dan angka."
                    else -> "Gagal memperbarui password. Silakan coba lagi."
                }
                showError("Gagal", errorMessage)
                isSuccess = false
            } finally {
                isLoading = false
            }
        }
    }

}