package dev.ferynnd.tugasakhir.data.remote.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.composeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import javax.inject.Inject


class SupabaseClient @Inject constructor(){

    // Ganti dengan kredensial dari Dashboard Supabase -> Settings -> API

    private val _client = createSupabaseClient(
        supabaseUrl = "https://dgosuntfquttgeqhtsvr.supabase.co",
        supabaseKey = "sb_publishable_66ml-4eJDwGHIaVqOuq1oA_ArtLuSKW"
    ) {
        // Install plugin yang dibutuhkan
        install(Auth)
        install(Postgrest)
        install(Storage)
        install(ComposeAuth){
            googleNativeLogin(serverClientId = "985225087240-6d2cvl4ud6hjsm7kcvlp9didk4qp287c.apps.googleusercontent.com")
        }
    }

    val auth get() = _client.auth
    val postgrest get() = _client.postgrest
    val storage get() = _client.storage
    val composeAuth get() = _client.composeAuth
}

