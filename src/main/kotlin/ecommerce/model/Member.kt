package ecommerce.model

import ecommerce.dto.MemberDto

class Member(
    val id: Long? = null,
    val email: String,
    val password: String,
    val role: String = "USER",
) {
    fun toDto(): MemberDto {
        return MemberDto(id!!, email)
    }
}
