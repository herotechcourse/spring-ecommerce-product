package ecommerce.service

import ecommerce.auth.JwtProvider
import ecommerce.repository.MemberRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(private val memberRepository: MemberRepository) {
    fun login(
        email: String,
        password: String,
    ): String {
        val member =
            memberRepository.findByEmail(email)
                ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        if (member.password != password) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }
        return JwtProvider.generateToken(member.email)
    }

    fun extractAndValidateToken(authHeader: String): String {
        val token = authHeader.removePrefix("Bearer ").trim()
        if (!JwtProvider.validateToken(token)) {
            throw IllegalArgumentException("Invalid token")
        }
        return JwtProvider.getEmailFromToken(token)
    }
}
