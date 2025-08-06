package ecommerce.controller

import ecommerce.dto.LoginRequest
import ecommerce.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<Map<String, String>> {
        val token = authService.login(request.email, request.password)
        return ResponseEntity.ok(mapOf("token" to token))
    }
}
