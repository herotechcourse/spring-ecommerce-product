package ecommerce.dto

data class ActiveUsersResponse(
    val memberId: Long,
    val memberName: String,
    val memberEmail: String,
)
