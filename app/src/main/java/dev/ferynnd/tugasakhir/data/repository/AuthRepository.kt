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
        var finalEmail = identifier

        // Jika input bukan email, cari emailnya berdasarkan username
        if (!identifier.contains("@")) {
            try {
                val response = client.postgrest["users"]
                    .select {
                        filter {
                            // Menggunakan ilike agar tidak masalah huruf besar/kecil
                            eq("username", identifier)
                        }
                    }.decodeSingleOrNull<User>() // Lebih aman daripada decodeSingle

                if (response != null) {
                    finalEmail = response.email
                    Log.d("AUTH_DEBUG", "Email ditemukan: $finalEmail")
                } else {
                    Log.d("AUTH_DEBUG", "Username tidak ditemukan")
                    throw Exception("Username tidak ditemukan")
                }
            } catch (e: Exception) {
                throw Exception("Gagal mencari data user: ${e.message}")
            }
        }

        // Eksekusi Login ke Supabase Auth
        try {
        client.auth.signInWith(Email) {
                this.email = finalEmail
                this.password = pass
            }
        } catch (e: Exception) {
            Log.e("AUTH_DEBUG", "Error di Supabase Auth (Cek Password): ${e.message}")
            throw e
        }
    }

}