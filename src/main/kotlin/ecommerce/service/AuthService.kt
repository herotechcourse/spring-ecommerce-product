package ecommerce.service

import ecommerce.auth.JwtTokenProvider
import ecommerce.dao.JdbcMemberDAO
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
    private val jdbcMemberDAO: JdbcMemberDAO,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun registerMember(form: RegisterForm): Member {
        checkMemberEmailExists(form.email)
        val member = Member.from(form)
        val id = jdbcMemberDAO.insert(member)
        return jdbcMemberDAO.findById(id)
            ?: throw InternalServerErrorException("AuthService.register() - Member with ID $id not found")
    }

    fun loginMember(form: LoginForm): AuthResponse {
        val member = jdbcMemberDAO.findByEmail(form.email) ?: throw AuthorizationException("Invalid email")
        if (member.password != form.password) throw AuthorizationException("Invalid password")
        val accessToken = jwtTokenProvider.createToken(member.email)
        return AuthResponse(accessToken)
    }

    fun findMemberById(id: Long): Member? = jdbcMemberDAO.findById(id)

    fun findMemberByEmail(email: String): Member? = jdbcMemberDAO.findByEmail(email)

    fun findMemberByToken(token: String): Member? {
        if (!jwtTokenProvider.validateToken(token)) {
            throw AuthorizationException("Invalid token")
        }
        val email = jwtTokenProvider.getPayload(token)
        return jdbcMemberDAO.findByEmail(email) ?: throw AuthorizationException("Invalid email")
    }

    private fun checkMemberEmailExists(
        email: String,
        originalEmail: String? = null,
    ) {
        if (originalEmail != null && email == originalEmail) {
            return
        } else if (jdbcMemberDAO.existsByEmail(email)) {
            val message = "Email $email already exists."
            throw MemberEmailAlreadyExistsException(message)
        }
    }
}
