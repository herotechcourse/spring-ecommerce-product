package ecommerce.service

import ecommerce.dto.auth.AuthTokenPayload
import ecommerce.dto.auth.LoginRequest
import ecommerce.dto.user.MemberUserDTO
import ecommerce.dto.user.UserCreateResponse
import ecommerce.dto.user.UserRequestDTO
import ecommerce.exception.UserAlreadyExistsException
import ecommerce.infrastructure.JwtTokenProvider
import ecommerce.repository.CartRepository
import ecommerce.repository.UserRepository
import org.springframework.stereotype.Service
import java.net.URI

@Service
class MemberAuthService(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val loginService: LoginService,
) {
    fun signUp(user: UserRequestDTO): UserCreateResponse {
        if (userRepository.existsByEmail(user.email)) {
            throw UserAlreadyExistsException(user.email)
        }
        val member =
            MemberUserDTO(
                email = user.email,
                password = user.password,
                name = user.name,
            )
        val id = userRepository.create(member)
        cartRepository.createCartForUser(id)
        val authTokenPayload = jwtTokenProvider.createToken(AuthTokenPayload(member.email))
        return UserCreateResponse(URI.create("/users/$id"), "Bearer $authTokenPayload")
    }

    fun logIn(loginRequest: LoginRequest): String {
        return loginService.signIn(loginRequest)
    }
}
