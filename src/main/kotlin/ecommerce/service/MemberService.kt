package ecommerce.service

import ecommerce.auth.JwtTokenProvider
import ecommerce.dto.MemberRequest
import ecommerce.dto.TokenResponse
import ecommerce.model.Member
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun register(request: MemberRequest): TokenResponse {
        val existing = memberRepository.findByEmail(request.email)
        if (existing != null) {
            throw IllegalArgumentException("Email already in use")
        }

        // ❗ SECURITY WARNING: For learning purposes only
        val saved = memberRepository.save(Member(email = request.email, password = request.password))
        val token = jwtTokenProvider.createToken(saved.id)
        return TokenResponse(token)
    }

    fun login(request: MemberRequest): TokenResponse {
        val member =
            memberRepository.findByEmail(request.email)
                ?: throw IllegalArgumentException("Invalid email or password")

        if (member.password != request.password) {
            throw IllegalArgumentException("Invalid email or password")
        }

        val token = jwtTokenProvider.createToken(member.id)
        return TokenResponse(token)
    }

    fun findByToken(token: String): Member {
        val memberId =
            jwtTokenProvider.getSubject(token).toLongOrNull()
                ?: throw IllegalArgumentException("Invalid token")
        return memberRepository.findById(memberId)
            ?: throw IllegalArgumentException("Member not found")
    }
}
