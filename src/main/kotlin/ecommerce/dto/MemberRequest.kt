package ecommerce.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

class MemberRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email must not be blank")
    val email: String,
    //    @field:Pattern(
//        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}\$",
//        message = "Password must contain letters, digits, and special characters"
//    )
    @field:Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    val password: String,
)
