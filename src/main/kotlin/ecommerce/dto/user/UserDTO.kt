package ecommerce.dto.user

import ecommerce.enums.UserRole

class UserDTO(
    val id: Long? = null,
    val email: String,
    val password: String,
    val name: String,
    val role: UserRole = UserRole.USER,
)
