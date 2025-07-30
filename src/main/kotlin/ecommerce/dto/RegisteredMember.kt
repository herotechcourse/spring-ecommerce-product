package ecommerce.dto

class MemberDto(
    val id: Long,
    val email: String,
    val role: Role,
)

enum class Role {
    ADMIN,
    USER,
}
