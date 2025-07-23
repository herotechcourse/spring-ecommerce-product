package ecommerce.dto.auth

import ecommerce.enums.UserRole

data class AuthTokenPayload(
    val email: String,
    val role: UserRole,
)
