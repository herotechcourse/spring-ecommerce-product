package ecommerce.auth

import ecommerce.dto.MemberDTO
import ecommerce.repository.MemberRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService (private val memberRepository: MemberRepository){
    fun login(memberDTO: MemberDTO): String {
        val member = memberRepository.findByEmail(memberDTO.email)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        if (member.password != memberDTO.password) {
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
