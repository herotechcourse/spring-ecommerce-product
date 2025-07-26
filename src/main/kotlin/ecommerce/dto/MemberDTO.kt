package ecommerce.dto

import ecommerce.model.Member

data class MemberDTO(
    var email: String,
    var password: String,
) {
    fun toEntity(): Member {
        return Member(
            email = this.email,
            password = this.password
        )
    }
}
