package ecommerce.config.interceptor

import ecommerce.dto.user.UserDTO
import ecommerce.enums.UserRole
import ecommerce.exception.UnauthorisedUserException
import ecommerce.infrastructure.JwtProvider
import ecommerce.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class MemberInterceptor(
    userRepository: UserRepository,
    jwtProvider: JwtProvider,
) : BaseAuthInterceptor(jwtProvider, userRepository) {
    override fun handleAuthenticatedRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        user: UserDTO,
    ): Boolean {
        if (user.role != UserRole.USER) {
            throw UnauthorisedUserException("Only User allowed")
        }

        return true
    }
}
