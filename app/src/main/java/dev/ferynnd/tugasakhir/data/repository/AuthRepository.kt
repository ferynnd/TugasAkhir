package dev.ferynnd.tugasakhir.data.repository

import android.util.Log
import dev.ferynnd.tugasakhir.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository(private val client: SupabaseClient) {

    suspend fun signUp(email: String, pass: String, username: String) {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = pass
            // Metadata ini akan masuk ke auth.users raw_user_meta_data
            data = buildJsonObject {
                put("username", username)
            }
        }
    }

    suspend fun signIn(identifier: String, pass: String) {
        try {
            client.auth.signInWith(Email) {
                    this.email = identifier
                    this.password = pass
            }
        } catch (e: Exception) {
            throw e
        }
    }




}