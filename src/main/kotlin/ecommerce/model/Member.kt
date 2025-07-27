package ecommerce.model

import ecommerce.dto.LoginForm
import ecommerce.dto.RegisterForm

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
    }
}
