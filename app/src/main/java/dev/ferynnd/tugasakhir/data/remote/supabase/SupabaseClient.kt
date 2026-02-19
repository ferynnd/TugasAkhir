package dev.ferynnd.tugasakhir.data.remote.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage


object SupabaseClient {

    // Ganti dengan kredensial dari Dashboard Supabase -> Settings -> API

    val client = createSupabaseClient(
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
}
