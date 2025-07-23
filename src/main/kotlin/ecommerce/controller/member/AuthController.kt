package ecommerce.controller.member

import ecommerce.dto.auth.TokenRequest
import ecommerce.dto.user.UserDTO
import ecommerce.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    val authService: AuthService,
) {
    @PostMapping("/signUp")
    fun signUp(
        @RequestBody @Valid userDTO: UserDTO,
    ): ResponseEntity<Void> {
        val userCreateResponse = authService.signUp(userDTO)
        return ResponseEntity.created(
            userCreateResponse.uri,
        )
            .header("Authorization", userCreateResponse.token)
            .build()
    }

    @PostMapping("/signIn")
    fun signIn(
        @RequestBody @Valid tokenRequest: TokenRequest,
    ): ResponseEntity<Void> {
        val token = authService.logIn(tokenRequest)
        return ResponseEntity.ok().header("Authorization", token).build()
    }
}