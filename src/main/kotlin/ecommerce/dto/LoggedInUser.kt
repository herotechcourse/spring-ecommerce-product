package ecommerce.dto

data class LoggedInUser(
    val id: Long,
    val email: String,
    val role: String,
)
