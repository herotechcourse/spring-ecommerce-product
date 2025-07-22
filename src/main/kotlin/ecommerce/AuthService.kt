package ecommerce

import jakarta.validation.ValidationException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository
) {
    fun createToken(tokenRequest: TokenRequest): TokenResponse {
        val member = memberRepository.findByEmail(tokenRequest.email)
            ?: throw AuthorizationException()
        if (member.password != tokenRequest.password) {
            throw AuthorizationException()
        }
        val accessToken = jwtTokenProvider.createToken(member.email)
        return TokenResponse(accessToken)
    }

    fun register(tokenRequest: TokenRequest): TokenResponse {
        val member = Member(email = tokenRequest.email, password = tokenRequest.password)
        memberRepository.insert(member)
        val accessToken = jwtTokenProvider.createToken(member.email)
        return TokenResponse(accessToken)
    }

    fun findMemberByToken(token: String): MemberResponse {
        if (!jwtTokenProvider.validateToken(token)) {
            throw AuthorizationException()
        }
        val email = jwtTokenProvider.getPayload(token)
        val member = memberRepository.findByEmail(email)
            ?: throw AuthorizationException()
        return MemberResponse(member.id!!, member.email)
    }
}