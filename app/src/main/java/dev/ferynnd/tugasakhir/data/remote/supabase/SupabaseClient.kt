package dev.ferynnd.tugasakhir.data.remote.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    // Ganti dengan kredensial dari Dashboard Supabase -> Settings -> API
    private const val SUPABASE_URL = "https://dgosuntfquttgeqhtsvr.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_66ml-4eJDwGHIaVqOuq1oA_ArtLuSKW"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        // Install plugin yang dibutuhkan
        install(Auth)
        install(Postgrest)
        install(ComposeAuth){
            googleNativeLogin(serverClientId = "985225087240-6d2cvl4ud6hjsm7kcvlp9didk4qp287c.apps.googleusercontent.com")
        }
    }
}