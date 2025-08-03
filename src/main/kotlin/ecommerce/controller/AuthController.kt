package ecommerce.controller

import ecommerce.dto.TokenRequest
import ecommerce.dto.TokenResponse
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
    @PostMapping("/login")
    fun tokenLogin(
        @Valid @RequestBody tokenRequest: TokenRequest,
    ): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.login(tokenRequest)
        return ResponseEntity.ok().body(tokenResponse)
    }

    @PostMapping("/register")
    fun tokenRegister(
        @Valid @RequestBody tokenRequest: TokenRequest,
    ): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.register(tokenRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse)
    }
}
