package dev.ferynnd.tugasakhir.data.repository

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository(private val client: SupabaseClient) {

    suspend fun signUp(email: String, pass: String, fullname: String) {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = pass
            data = buildJsonObject {
                put("full_name", fullname)
            }
        }
        Log.d("AuthRepository", "Sign-up successful")
    }

    suspend fun signIn(email: String, pass: String) {
        try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = pass
            }
            Log.d("AuthRepository", "Sign-in successful")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error during sign-in: ${e.message}", e)
            throw e
        }
    }

}