package ecommerce.service

import ecommerce.dto.auth.LoginRequest
import org.springframework.stereotype.Service

@Service
class AdminAuthService(
    private val loginService: LoginService,
) {
    fun signIn(loginRequest: LoginRequest): String {
        return loginService.signIn(loginRequest)
    }
}
