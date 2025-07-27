package ecommerce.service

import ecommerce.dto.member.LoginRequest
import ecommerce.dto.member.RegisterRequest
import ecommerce.exception.auth.InvalidCredentialsException
import ecommerce.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberService: MemberService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun register(request: RegisterRequest): String {
        memberService.createMember(request.email, request.password, name = request.name)
        val authentication =
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password),
            )
        return jwtTokenProvider.generateToken(authentication)
    }

    fun login(request: LoginRequest): String {
        return try {
            val authentication =
                authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(request.email, request.password),
                )
            jwtTokenProvider.generateToken(authentication)
        } catch (e: AuthenticationException) {
            throw InvalidCredentialsException("Invalid email or password.")
        }
    }
}
