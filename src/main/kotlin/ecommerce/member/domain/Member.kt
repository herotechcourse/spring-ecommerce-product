package ecommerce.member.domain

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class Member(var id: Long? = null,
                  @field:NotBlank(message = "Email cannot be blank")
                  @field:Email(message = "Email must be a valid email address")
                  val email: String,
                  @field:NotBlank(message = "Password cannot be blank")
                  @field:Size(min = 8, message = "Password must be at least 8 characters long")
                  val password: String,
                  val role: String = "USER")