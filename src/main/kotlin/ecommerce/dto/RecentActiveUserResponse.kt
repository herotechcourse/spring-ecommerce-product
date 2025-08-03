package ecommerce.dto

data class RecentActiveUserResponse(
    val userId: Long,
    val name: String,
    val email: String,
)
