package ecommerce.auth.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class Member(
    val id: Long? = null,
    @field:NotNull(message = "Email is required")
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email is required")
    val email: String,
    @field:NotNull(message = "Password is required")
    @field:NotBlank(message = "Password is required")
    val password: String,
    @field:NotNull(message = "Name is required")
    @field:NotBlank(message = "Name is required")
    val name: String,
)
