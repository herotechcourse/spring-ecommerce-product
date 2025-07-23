package ecommerce.service

import ecommerce.configuration.JwtTokenProvider
import ecommerce.configuration.PasswordEncoder
import ecommerce.dto.RegistrationRequest
import ecommerce.dto.TokenResponse
import ecommerce.exception.EmailOrPasswordIncorrectException
import ecommerce.exception.MemberEmailAlreadyExistsException
import ecommerce.model.Member
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticationService(
    private val memberRepository: MemberRepository,
    private val tokenService: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun registerMember(request: RegistrationRequest): TokenResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw MemberEmailAlreadyExistsException("Email already exists: ${request.email}")
        }

        val hashedPassword = passwordEncoder.encode(request.password)
        val member = Member(null, request.email, hashedPassword)

        val saved = memberRepository.save(member)

        if (!saved) throw RuntimeException("Failed to save member with email: ${request.email} to the database")

        val token = tokenService.createToken(request.email)
        return TokenResponse(token)
    }

    fun logIn(request: RegistrationRequest): TokenResponse {
        val member = memberRepository.findByEmail(request.email) ?: throw EmailOrPasswordIncorrectException("Invalid password for email")

        if (!passwordEncoder.matches(request.password, member.password)) {
            throw EmailOrPasswordIncorrectException("Invalid password for email")
        }
        val token = tokenService.createToken(request.email)
        return TokenResponse(token)
    }
}
