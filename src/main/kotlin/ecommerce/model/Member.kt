package ecommerce.model

import ecommerce.dto.MemberDto

class Member(
    val id: Long? = null,
    val email: String,
    val password: String,
    val role: String,
) {
    fun toDto(): MemberDto {
        return MemberDto(id!!, email, role)
    }
}
