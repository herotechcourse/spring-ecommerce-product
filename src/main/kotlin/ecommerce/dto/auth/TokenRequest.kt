package ecommerce.dto.auth

data class TokenRequest(
    val email: String,
    val password: String,
)
