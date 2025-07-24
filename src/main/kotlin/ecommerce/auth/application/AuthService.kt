package ecommerce.auth.application

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
            throw AuthException()
        }
        return createToken(tokenRequest)
    }

    fun checkInvalidLogin(tokenRequest: TokenRequest): Boolean {
        val allMembers = memberService.findAll()
        return allMembers.none { it.email == tokenRequest.email && it.password == tokenRequest.password }
    }

    fun findMemberByToken(token: String): MemberDTO {
        val payload = jwtTokenProvider.getPayload(token)
        val foundMember = memberService.findMember(payload)
        return MemberDTO.from(foundMember)
    }
}
