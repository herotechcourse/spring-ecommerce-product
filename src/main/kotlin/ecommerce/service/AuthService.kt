package ecommerce.service

import ecommerce.auth.JwtTokenProvider
import ecommerce.dao.JdbcMemberDao
import ecommerce.dto.AuthResponse
import ecommerce.dto.LoginForm
import ecommerce.dto.RegisterForm
import ecommerce.exception.AuthorizationException
import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.MemberEmailAlreadyExistsException
import ecommerce.model.Member
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jdbcMemberDao: JdbcMemberDao,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun registerMember(form: RegisterForm): Member {
        checkMemberEmailExists(form.email)
        val member = Member.from(form)
        val id = jdbcMemberDao.insert(member)
        return jdbcMemberDao.findById(id)
            ?: throw InternalServerErrorException(MESSAGE_MEMBER_NOT_FOUND)
    }

    fun loginMember(form: LoginForm): AuthResponse {
        val member = jdbcMemberDao.findByEmail(form.email) ?: throw AuthorizationException(MESSAGE_INVALID_EMAIL)
        if (member.password != form.password) throw AuthorizationException(MESSAGE_INVALID_PASSWORD)
        val accessToken = jwtTokenProvider.createToken(member.email)
        return AuthResponse(accessToken)
    }

    fun findMemberById(id: Long): Member? = jdbcMemberDao.findById(id)

    fun findMemberByEmail(email: String): Member? = jdbcMemberDao.findByEmail(email)

    fun findMemberByToken(token: String): Member {
        if (!jwtTokenProvider.validateToken(token)) {
            throw AuthorizationException(MESSAGE_INVALID_TOKEN)
        }
        val email = jwtTokenProvider.getPayload(token)
        val member = jdbcMemberDao.findByEmail(email) ?: throw AuthorizationException(MESSAGE_INVALID_EMAIL)
        return member
    }

    private fun checkMemberEmailExists(
        email: String,
        originalEmail: String? = null,
    ) {
        if (originalEmail != null && email == originalEmail) {
            return
        } else if (jdbcMemberDao.existsByEmail(email)) {
            throw MemberEmailAlreadyExistsException(MESSAGE_EMAIL_ALREADY_EXISTS)
        }
    }

    companion object {
        const val MESSAGE_MEMBER_NOT_FOUND = "Member not found"
        const val MESSAGE_INVALID_EMAIL = "Invalid email"
        const val MESSAGE_INVALID_PASSWORD = "Invalid password"
        const val MESSAGE_INVALID_TOKEN = "Invalid token"
        const val MESSAGE_EMAIL_ALREADY_EXISTS = "Email already exists"
    }
}
