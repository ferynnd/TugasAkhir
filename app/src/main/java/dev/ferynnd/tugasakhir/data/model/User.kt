package dev.ferynnd.tugasakhir.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Profile(
    val id: String,          // UUID dari auth.users
    @SerialName("full_name")
    val fullName: String?,
    val email: String?,
    @SerialName("avatar_url")
    val avatar: String?,
    @SerialName("created_at")
    val createdAt: String
)

enum class Gender {
    MALE, FEMALE
}

enum class CategoryBmi {
    UNDERWEIGHT, NORMAL, OVERWEIGHT, OBESE
}

@Serializable
data class UserBmi(
    val id: Int? = null,
    @SerialName("user_id")
    val userId: String,
    val gender: Gender,
    val height: Int,     // cm
    val weight: Int,     // kg
    val age: Int,
    @SerialName("bmi_value")
    val bmiValue: Double,
    @SerialName("category_value")
    val categoryValue: CategoryBmi,
    @SerialName("created_at")
    val createdAt: String? = null
)
