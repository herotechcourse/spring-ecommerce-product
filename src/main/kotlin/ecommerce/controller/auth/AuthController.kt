package ecommerce.controller.auth

import ecommerce.dto.member.AuthResponse
import ecommerce.dto.member.LoginRequest
import ecommerce.dto.member.RegisterRequest
import ecommerce.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest,
    ): ResponseEntity<AuthResponse> {
        val token = authService.register(request)
        return ResponseEntity(AuthResponse(token), HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<AuthResponse> {
        val token = authService.login(request)
        return ResponseEntity(AuthResponse(token), HttpStatus.OK)
    }
}
