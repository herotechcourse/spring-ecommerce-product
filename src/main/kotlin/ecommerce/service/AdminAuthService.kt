package ecommerce.service

import ecommerce.dto.auth.LoginRequest
import ecommerce.enums.UserRole
import org.springframework.stereotype.Service

@Service
class AdminAuthService(
    private val loginService: LoginService,
) {
    fun signIn(loginRequest: LoginRequest): String {
        return loginService.signIn(loginRequest, UserRole.ADMIN)
    }
}
