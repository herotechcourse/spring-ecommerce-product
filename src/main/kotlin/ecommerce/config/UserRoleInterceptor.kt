package ecommerce.config

import ecommerce.service.JwtService
import ecommerce.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerInterceptor

@Component
class UserRoleInterceptor(
    private val jwtService: JwtService,
    private val userService: UserService,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val authHeader =
            request.getHeader("Authorization")
                ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization header")

        if (!authHeader.startsWith("Bearer ")) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Authorization header format")
        }

        val token = authHeader.removePrefix("Bearer ").trim()
        val email = jwtService.validateAndExtractEmail(token)

        val user =
            userService.getByEmail(email)
                ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")

        request.setAttribute("authenticatedUser", user)

        return true
    }
}
