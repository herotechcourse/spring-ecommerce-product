package ecommerce.dto

import ecommerce.model.Member
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class MemberRequestDTO(
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
) {
    fun toEntity(): Member {
        return Member(
            email = email,
            password = password,
            name = name,
        )
    }
}
