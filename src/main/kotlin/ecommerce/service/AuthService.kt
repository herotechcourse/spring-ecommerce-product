package ecommerce.service

import ecommerce.dto.AuthTokenPayload
import ecommerce.dto.TokenRequest
import ecommerce.dto.UserCreateResponse
import ecommerce.dto.UserDTO
import ecommerce.exception.EntityNotFoundException
import ecommerce.exception.UserAlreadyExistsException
import ecommerce.infrastructure.JwtTokenProvider
import ecommerce.repository.UserRepository
import org.springframework.stereotype.Service
import java.net.URI

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun signUp(user: UserDTO): UserCreateResponse {
        if (userRepository.existsByEmail(user.email)) {
            throw UserAlreadyExistsException(user.email)
        }
        val id = userRepository.create(user)
        val authTokenPayload = jwtTokenProvider.createToken(AuthTokenPayload(user.email, user.role))
        return UserCreateResponse(URI.create("/users/$id"), authTokenPayload)
    }

    fun logIn(tokenRequest: TokenRequest): String {
        val user =
            userRepository.findByEmailAndPassword(
                tokenRequest.email, tokenRequest.password,
            ) ?: throw EntityNotFoundException("User with email ${tokenRequest.email} not found")

        return jwtTokenProvider.createToken(
            AuthTokenPayload(user.email, user.role),
        )
    }

    fun logOut(token: String): String {
        // TODO implement TODO
        return ""
    }
}
