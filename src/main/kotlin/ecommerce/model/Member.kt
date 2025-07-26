package ecommerce.model

import java.util.UUID

data class Member(
    val id: UUID = UUID.randomUUID(),
    var email: String,
    var password: String,
    var role: MemberRole = MemberRole.ROLE_USER,
)

enum class MemberRole {
    ROLE_USER,
    ROLE_ADMIN,
}
