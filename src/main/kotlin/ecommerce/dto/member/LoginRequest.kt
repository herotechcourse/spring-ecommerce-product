package ecommerce.dto.member

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Email cannot be empty")
    @field:Email(message = "Email should be valid")
    val email: String,
    @field:NotBlank(message = "Password cannot be empty")
    val password: String,
)
