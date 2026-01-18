package dev.ferynnd.tugasakhir.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    val id: @Contextual UUID = UUID.randomUUID() ,
    val username: String = "",
    val email: String = "",
    val password: String? = null,
)