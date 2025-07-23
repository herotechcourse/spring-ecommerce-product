package ecommerce.dto.member

class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String,
    val userName: String,
)
