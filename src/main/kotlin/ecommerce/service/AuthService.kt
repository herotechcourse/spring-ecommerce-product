package ecommerce.service

import ecommerce.dto.MemberDto
import ecommerce.dto.TokenRequest
import ecommerce.dto.TokenResponse
import ecommerce.exception.NotFoundException
import ecommerce.exception.UnauthorizedException
import ecommerce.infrastructure.JwtTokenProvider
import ecommerce.model.Member
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository,
) {
    fun findMember(payload: String): MemberDto {
        val member = memberRepository.findByEmail(payload)
        if (member == null) {
            throw NotFoundException("Member not found")
            // or is it better to return 500?
        }
        // TODO check other options for retrieval of member id when it is null
        return MemberDto(member.id!!, member.email, member.role)
    }

    fun findMemberByToken(token: String): MemberDto {
        if (!jwtTokenProvider.validateToken(token)) {
            throw UnauthorizedException("Invalid token")
        }
        val payload = jwtTokenProvider.getPayload(token)
        return findMember(payload)
    }

    fun register(request: TokenRequest): TokenResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Account with email already exists")
        }
        memberRepository.registerMember(Member(email = request.email, password = request.password))
        return createToken(request)
    }

    fun login(request: TokenRequest): TokenResponse {
        if (!memberRepository.existsByEmail(request.email)) {
            throw NotFoundException("No account with email exists")
        }
        val member = Member(email = request.email, password = request.password)
        if (memberRepository.findMember(member) == null) {
            throw UnauthorizedException("Incorrect Password")
        }
        return createToken(request)
    }

    fun createToken(tokenRequest: TokenRequest): TokenResponse {
        val accessToken = jwtTokenProvider.createToken(tokenRequest.email)
        return TokenResponse(accessToken)
    }
}
