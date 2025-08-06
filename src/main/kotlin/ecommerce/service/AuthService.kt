package ecommerce.service

import ecommerce.auth.JwtProvider
import ecommerce.exception.UnauthorizedException
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(private val memberRepository: MemberRepository) {
    fun login(
        email: String,
        password: String,
    ): String {
        val member =
            memberRepository.findByEmail(email) ?: throw UnauthorizedException()
        if (member.password != password) {
            throw UnauthorizedException()
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
