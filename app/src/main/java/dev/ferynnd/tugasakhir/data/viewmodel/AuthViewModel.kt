package dev.ferynnd.tugasakhir.data.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ferynnd.tugasakhir.data.helper.SharedPreferenceHelper
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository,  private val pref: SharedPreferenceHelper) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)


    var isDialogVisible by mutableStateOf(false)
        private set

    var dialogTitle by mutableStateOf("")
        private set

    var dialogMessage by mutableStateOf("")
        private set

    private fun showDialog(title: String, message: String) {
        dialogTitle = title
        dialogMessage = message
        isDialogVisible = true
    }

    fun dismissDialog() {
        isDialogVisible = false
    }


    fun validateInputRegister(
        email: String,
        pass: String,
        confirmPass: String,
        username: String,
        isChecked: Boolean
    ) {
        when {
            username.isBlank() ->
                showDialog("Error", "Username tidak boleh kosong")

            username != username.lowercase() ->
                showDialog("Error", "Username harus menggunakan huruf kecil")

            email.isBlank() ->
                showDialog("Error", "Email tidak boleh kosong")

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                showDialog("Error", "Email tidak valid")

            pass.length < 6 ->
                showDialog("Error", "Password minimal 6 karakter")

            pass != confirmPass ->
                showDialog("Error", "Password tidak sama")

            !isChecked ->
                showDialog("Error", "Harus menyetujui syarat dan ketentuan")

            else -> {
                showDialog("Berhasil", "Akun berhasil dibuat")
                onRegister(email, pass, username)
            }
        }
    }


    private fun onRegister(email: String, pass: String, username: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                repository.signUp(email, pass, username)
                isSuccess = true
            } catch (e: Exception) {
                showDialog("Error", e.localizedMessage ?: "Registrasi Gagal")
            } finally {
                isLoading = false
            }
        }
    }

    fun validateInputLogin(
        email: String,
        pass: String
    ) {
        when {
            email.isBlank() ->
                showDialog("Error", "Email tidak boleh kosong")
            pass.isBlank() ->
                showDialog("Error", "Password tidak boleh kosong")
            else -> {
                onLogin(email, pass)
            }
        }
    }


    fun onLogin(identifier: String, pass: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                repository.signIn(identifier, pass)
//                pref.setLogin(true, identifier)
                isSuccess = true
            } catch (e: Exception) {
                 val message = when {
                    e.message?.contains("Invalid login credentials", true) == true ->
                        "Email atau password salah"
                    e.message?.contains("Email not confirmed", true) == true ->
                        "Email belum diverifikasi"
                    else ->
                        "Login gagal, silakan coba lagi"
                }
                showDialog("Error", message)
            } finally {
                isLoading = false
            }
        }
    }

    fun onGoogleLoginSuccess(email: String?) {
        pref.setLogin(true, email)
    }

     fun logout() {
        viewModelScope.launch {
            SupabaseClient.client.auth.signOut() // logout Supabase
            pref.logout() // hapus state lokal
        }
    }


//    fun isUserLoggedIn(): Boolean {
//        return pref.isLogin()
//    }

     fun isUserLoggedIn(): Boolean {
        return SupabaseClient.client.auth.currentSessionOrNull() != null
    }

}