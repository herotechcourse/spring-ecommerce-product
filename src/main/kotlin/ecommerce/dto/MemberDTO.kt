package ecommerce.dto

import ecommerce.model.Member

data class MemberDTO(
    val id: Long,
    val email: String,
    val name: String,
) {
    companion object {
        fun from(member: Member): MemberDTO {
            return MemberDTO(
                id = member.id ?: 0,
                email = member.email,
                name = member.name,
            )
        }
    }
}
