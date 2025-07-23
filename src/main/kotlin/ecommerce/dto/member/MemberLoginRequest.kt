package ecommerce.dto.member

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MemberLoginRequest(
    @field:Email(message = "Email cannot be blank.")
    @field:NotBlank(message = "Email cannot be blank.")
    val email: String,
    @field:NotBlank(message = "Password cannot be blank.")
    @field:Size(min = 8, message = "Password must be at least 8 characters long.")
    val passwordHash: String,
)
