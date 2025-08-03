package ecommerce.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class TokenRequest(
    @field:NotNull(message = "Email cannot be blank")
    @field:NotBlank(message = "Email can't be empty")
    @field:Email(message = "Email cannot be blank")
    val email: String,
    @field:NotNull(message = "Password cannot be blank")
    @field:NotBlank(message = "Password can't be empty")
    val password: String,
)
