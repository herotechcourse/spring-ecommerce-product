package ecommerce.auth.model

data class TokenRequest(
    val email: String,
    val password: String,
)
