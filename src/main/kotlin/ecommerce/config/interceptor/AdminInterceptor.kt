package ecommerce.config.interceptor

import ecommerce.enums.UserRole
import ecommerce.exception.UnauthorisedUserException
import ecommerce.infrastructure.JwtTokenProvider
import ecommerce.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class AdminInterceptor(
    val userRepository: UserRepository,
    jwtTokenProvider: JwtTokenProvider,
) : BaseAuthInterceptor(jwtTokenProvider) {
    override fun handleAuthenticatedRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        email: String,
    ): Boolean {
        val user =
            userRepository.findByEmail(email)
                ?: throw UnauthorisedUserException("User not found")

        if (user.role != UserRole.ADMIN) {
            throw UnauthorisedUserException("User role not admin")
        }

        return true
    }
}
