package ecommerce.auth.application

import ecommerce.auth.exception.AuthException
import ecommerce.auth.infrastructure.JwtTokenProvider
import ecommerce.auth.model.MemberDTO
import ecommerce.auth.model.TokenRequest
import ecommerce.auth.model.TokenResponse
import ecommerce.auth.service.MemberService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberService: MemberService,
) {
    fun createToken(tokenRequest: TokenRequest): TokenResponse {
        val accessToken = jwtTokenProvider.createToken(tokenRequest.email)
        return TokenResponse(accessToken)
    }

    fun issueTokenToLoggedUSer(tokenRequest: TokenRequest): TokenResponse {
        if (checkInvalidLogin(tokenRequest)) {
            throw AuthException(message = "Invalid login")
        }
        return createToken(tokenRequest)
    }

    fun checkInvalidLogin(tokenRequest: TokenRequest): Boolean {
        val allMembers = memberService.findAll()
        return allMembers.none { it.email == tokenRequest.email && it.password == tokenRequest.password }
    }

    fun findMemberByToken(token: String): MemberDTO {
        val payload = jwtTokenProvider.getPayload(token)
            ?: throw AuthException("Invalid token or payload missing")

        val foundMember = memberService.findMember(payload)
            ?: throw AuthException("Member not found")

        return MemberDTO.from(foundMember)
    }
}
