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
    var errorMessage by mutableStateOf<String?>(null)

    fun onRegister(email: String, pass: String, username: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                repository.signUp(email, pass, username)
                isSuccess = true
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Registrasi Gagal"
            } finally {
                isLoading = false
            }
        }
    }

    fun onLogin(identifier: String, pass: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                repository.signIn(identifier, pass)
                pref.setLogin(true, identifier)
                isSuccess = true
            } catch (e: Exception) {
                Log.e("AUTH_DEBUG", "Terjadi kesalahan saat login", e)
                errorMessage = "Username/Email atau Password salah"
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


    fun isUserLoggedIn(): Boolean {
        return pref.isLogin()
    }

}