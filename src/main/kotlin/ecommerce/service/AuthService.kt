package ecommerce.service

import ecommerce.dto.MemberResponse
import ecommerce.dto.TokenRequest
import ecommerce.dto.TokenResponse
import ecommerce.handler.AuthorizationException
import ecommerce.handler.ValidationException
import ecommerce.infrastructure.JWTProvider
import ecommerce.model.NewMember
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JWTProvider,
    private val memberRepository: MemberRepository,
) {
    fun createToken(tokenRequest: TokenRequest): TokenResponse {
        val member =
            memberRepository.findByEmail(tokenRequest.email)
                ?: throw AuthorizationException("Member not found with email: ${tokenRequest.email}")

        if (member.password != tokenRequest.password) {
            throw AuthorizationException("Invalid password for email: ${tokenRequest.email}")
        }

        val accessToken = jwtTokenProvider.createToken(member.email)
        return TokenResponse(accessToken)
    }

    fun register(tokenRequest: TokenRequest): TokenResponse {
        if (memberRepository.existsByEmail(tokenRequest.email)) {
            throw ValidationException("Email is already registered")
        }

        val role = if (tokenRequest.email == "admin@example.com") "ADMIN" else "USER"

        val newMember =
            NewMember(
                name = tokenRequest.name,
                email = tokenRequest.email,
                password = tokenRequest.password,
                role = role,
            )

        val member = memberRepository.insert(newMember)

        val accessToken = jwtTokenProvider.createToken(member.email)

        return TokenResponse(accessToken)
    }

    fun findMemberByToken(token: String): MemberResponse {
        if (!jwtTokenProvider.validateToken(token)) {
            throw AuthorizationException("Invalid or expired JWT token")
        }

        val email = jwtTokenProvider.getPayload(token)
        val member =
            memberRepository.findByEmail(email)
                ?: throw AuthorizationException("Member not found with email: $email")
        return MemberResponse(member.id, member.email, role = member.role, name = "John Doe")
    }
}
