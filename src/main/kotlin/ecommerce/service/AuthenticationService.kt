package ecommerce.service

import ecommerce.configuration.JwtTokenProvider
import ecommerce.configuration.PasswordEncoder
import ecommerce.dto.LoginRequest
import ecommerce.dto.RegistrationRequest
import ecommerce.dto.TokenResponse
import ecommerce.exception.EmailOrPasswordIncorrectException
import ecommerce.exception.MemberEmailAlreadyExistsException
import ecommerce.model.Member
import ecommerce.repository.CartRepository
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticationService(
    private val memberRepository: MemberRepository,
    private val tokenService: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val cartRepository: CartRepository,
) {
    @Transactional
    fun registerMember(request: RegistrationRequest): TokenResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw MemberEmailAlreadyExistsException("Email already exists: ${request.email}")
        }

        val hashedPassword = passwordEncoder.encode(request.password)
        val member = Member(null, request.name, request.email, hashedPassword, "USER")

        val memberId =
            memberRepository.save(member)
                ?: throw RuntimeException("Failed to save member with email: ${request.email} to the database")

        cartRepository.createCart(memberId)

        val token = tokenService.createToken(request.email)
        return TokenResponse(token)
    }

    fun logIn(request: LoginRequest): TokenResponse {
        val member = memberRepository.findByEmail(request.email) ?: throw EmailOrPasswordIncorrectException("Invalid password for email")

        if (!passwordEncoder.matches(request.password, member.password)) {
            throw EmailOrPasswordIncorrectException("Invalid password for email")
        }
        val token = tokenService.createToken(request.email)
        return TokenResponse(token)
    }
}
