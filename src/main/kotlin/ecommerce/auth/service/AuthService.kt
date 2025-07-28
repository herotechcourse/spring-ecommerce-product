package ecommerce.auth.service

import ecommerce.auth.exception.AuthorizationException
import ecommerce.auth.security.JwtProvider
import ecommerce.member.domain.Member
import ecommerce.member.dto.MemberResponse
import ecommerce.member.dto.TokenRequest
import ecommerce.member.dto.TokenResponse
import ecommerce.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtProvider: JwtProvider,
    private val memberRepository: MemberRepository,
) {
    fun createToken(tokenRequest: TokenRequest): TokenResponse {
        val member =
            memberRepository.findByEmail(tokenRequest.email)
                ?: throw AuthorizationException("Member not found with email: ${tokenRequest.email}")
        if (member.password != tokenRequest.password) {
            throw AuthorizationException("Invalid password for email: ${tokenRequest.email}")
        }
        val accessToken = jwtProvider.createToken(member.email)
        return TokenResponse(accessToken)
    }

    fun register(tokenRequest: TokenRequest): TokenResponse {
        val member =
            Member(
                email = tokenRequest.email,
                password = tokenRequest.password,
                role = if (tokenRequest.email == "admin@example.com") "ADMIN" else "USER",
            )
        memberRepository.insert(member)
        val accessToken = jwtProvider.createToken(member.email)
        return TokenResponse(accessToken)
    }

    fun findMemberByToken(token: String): MemberResponse {
        if (!jwtProvider.validateToken(token)) {
            throw AuthorizationException("Invalid or expired JWT token")
        }
        val email = jwtProvider.getPayload(token)
        val member =
            memberRepository.findByEmail(email)
                ?: throw AuthorizationException("Member not found with email: $email")
        return MemberResponse(member.id!!, member.email)
    }

    fun findMemberEntityByToken(token: String): Member {
        if (!jwtProvider.validateToken(token)) {
            throw AuthorizationException("Invalid or expired JWT token")
        }
        val email = jwtProvider.getPayload(token)
        return memberRepository.findByEmail(email)
            ?: throw AuthorizationException("Member not found with email: $email")
    }
}
