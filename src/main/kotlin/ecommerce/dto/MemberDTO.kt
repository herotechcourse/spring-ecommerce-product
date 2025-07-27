package ecommerce.dto

import ecommerce.model.Member
import ecommerce.model.Role

data class MemberDTO(
    var email: String,
    var password: String,
    var role: Role = Role.USER
) {
    fun toEntity(): Member {
        return Member(
            email = this.email,
            password = this.password,
            role = this.role
        )
    }
}
