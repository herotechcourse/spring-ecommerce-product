package ecommerce.dto.member

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String,
    val userName: String,
)
