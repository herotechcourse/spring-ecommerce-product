package ecommerce.service

import ecommerce.application.JwtTokenProvider
import ecommerce.dto.TokenRequest
import ecommerce.dto.TokenResponse
import ecommerce.exception.AuthException
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
}
