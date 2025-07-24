package ecommerce.dto

data class MemberDto(
    val id: Long,
    val email: String,
    val password: String,
    val role: String,
)
