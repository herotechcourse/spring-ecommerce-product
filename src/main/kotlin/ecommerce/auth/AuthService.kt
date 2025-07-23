package ecommerce.auth

import ecommerce.exception.NotFoundException
import ecommerce.exception.UnauthorizedException
import ecommerce.infrastructure.JwtTokenProvider
import ecommerce.model.Member
import ecommerce.model.TokenRequest
import ecommerce.model.TokenResponse
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository,
) {
//
//    fun findMember(principal: String): MemberResponse {
//        return MemberResponse(1L, principal, 10)
//    }
//
//    fun findMemberByToken(token: String): MemberResponse {
//        val payload = jwtTokenProvider.getPayload(token)
//        return findMember(payload)
//    }

    fun register(request: TokenRequest): TokenResponse {
        if (memberRepository.existsByEmail(request.email)) {
            // TODO customize exception
            throw IllegalArgumentException("Account with email already exists")
        }
        memberRepository.registerMember(Member(email = request.email, password = request.password))
        return createToken(request)
    }

    fun login(request: TokenRequest): TokenResponse {
        if (!memberRepository.existsByEmail(request.email)) {
            // TODO customize exception -> return 404
            throw NotFoundException("No account with email exists")
        }
        val member = Member(email = request.email, password = request.password)
        if (memberRepository.findMember(member) == null) {
            // TODO customize -> return 401
            throw UnauthorizedException("Incorrect Password")
        }
        return createToken(request)
    }

    fun createToken(tokenRequest: TokenRequest): TokenResponse {
        val accessToken = jwtTokenProvider.createToken(tokenRequest.email)
        return TokenResponse(accessToken)
    }
}
