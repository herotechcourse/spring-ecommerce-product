package ecommerce.service

import ecommerce.auth.JwtTokenProvider
import ecommerce.dto.TokenRequest
import ecommerce.dto.TokenResponse
import ecommerce.exception.AuthorizationException
import ecommerce.model.Member
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val service: MemberService,
) {
    fun register(tokenRequest: TokenRequest): TokenResponse {
        val member = Member(email = tokenRequest.email, password = tokenRequest.password)
        service.validateUniqueName(member)
        service.create(member)
        return TokenResponse(jwtTokenProvider.createToken(member.email))
    }

    fun login(tokenRequest: TokenRequest): TokenResponse {
        val member =
            service.findByEmail(tokenRequest.email)
                ?: throw AuthorizationException()
        if (member.password != tokenRequest.password) {
            throw AuthorizationException()
        }
        val accessToken = jwtTokenProvider.createToken(member.email)
        return TokenResponse(accessToken)
    }
}
