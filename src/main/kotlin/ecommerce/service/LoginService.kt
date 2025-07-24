package ecommerce.service

import ecommerce.dto.auth.AuthTokenPayload
import ecommerce.dto.auth.LoginRequest
import ecommerce.exception.UserCredentialException
import ecommerce.infrastructure.JwtTokenProvider
import ecommerce.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun signIn(loginRequest: LoginRequest): String {
        val user =
            userRepository.findByEmailAndPassword(
                loginRequest.email, loginRequest.password,
            ) ?: throw UserCredentialException()

        val token =
            jwtTokenProvider.createToken(
                AuthTokenPayload(user.email),
            )
        return "Bearer $token"
    }
}
