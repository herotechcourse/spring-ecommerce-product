package ecommerce.model

import ecommerce.exception.UnauthorizedException
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class Member(
    val id: Long? = null,
    @field:NotBlank(message = "Field cannot be blank")
    @field:Email(message = "Invalid email")
    val email: String,
    @field:NotBlank(message = "Field cannot be blank")
    @field:Size(min = 8, max = 30, message = "Invalid password: must between 8 and 30 characters")
    val password: String,
    @field:NotBlank(message = "Field cannot be blank")
    @field:Pattern(regexp = "^(user|admin)", message = "Invalid role")
    val role: String = "user",
) {
    fun validatePassword(password: String) {
        if (this.password != password) {
            throw UnauthorizedException("Incorrect password")
        }
    }
}
