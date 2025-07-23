package ecommerce.service

import ecommerce.configuration.JwtTokenProvider
import ecommerce.dto.RegistrationRequest
import ecommerce.dto.TokenResponse
import ecommerce.exception.MemberEmailAlreadyExistsException
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticationService(
    private val memberRepository: MemberRepository,
    private val tokenService: JwtTokenProvider,
) {
    @Transactional
    fun registerMember(request: RegistrationRequest): TokenResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw MemberEmailAlreadyExistsException("Email already exists: ${request.email}")
        }
        val saved = memberRepository.save(request)

        if (!saved) throw RuntimeException("Failed to save member with email: ${request.email} to the database")

        val token = tokenService.createToken(request.email)
        return TokenResponse(token)
    }
}
