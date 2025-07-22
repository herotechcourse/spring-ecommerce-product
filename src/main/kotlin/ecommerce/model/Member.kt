package ecommerce.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class Member(
    val id: Long? = null,

    @field:Email(message = "Invalid email")
    val email: String,

    @field:Size(min = 8, max = 30, message = "Invalid password: must between 8 and 30 characters")
    val password: String,

    @field:Pattern(regexp = "^(user|admin)", message = "Invalid role")
    val role: String = "user",
)
