package br.com.pluvia.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String
) {
    val initials: String
        get() {
            val parts = name.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
            return if (parts.size >= 2) {
                "${parts.first().first()}${parts.last().first()}".uppercase()
            } else {
                name.take(2).uppercase()
            }
        }
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirmation") val passwordConfirmation: String
)

data class AuthResponse(
    val token: String? = null,
    val user: User? = null,
    val message: String? = null
)

data class UpdateProfileRequest(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    @SerializedName("passwordConfirmation") val passwordConfirmation: String? = null
)

data class UpdateProfileResponse(
    val message: String,
    val user: User
)
