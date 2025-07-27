package ecommerce.model

import ecommerce.dto.LoginForm
import ecommerce.dto.MemberResponse
import ecommerce.dto.RegisterForm
import ecommerce.exception.InternalServerErrorException

data class Member(
    var id: Long? = null,
    var email: String,
    var password: String,
) {
    companion object {
        fun toEntity(
            member: Member,
            id: Long,
        ): Member {
            return Member(id, member.email, member.password)
        }

        fun from(loginForm: LoginForm): Member {
            return Member(email = loginForm.email, password = loginForm.password)
        }

        fun from(registerForm: RegisterForm): Member {
            return Member(email = registerForm.email, password = registerForm.password)
        }

        fun toResponse(entity: Member): MemberResponse {
            val id = entity.id ?: throw InternalServerErrorException("Member ID is null")
            return MemberResponse(id = id, email = entity.email)
        }
    }
}
