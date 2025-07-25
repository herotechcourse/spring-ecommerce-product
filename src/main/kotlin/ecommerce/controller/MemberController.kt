package ecommerce.controller

import ecommerce.dto.LoginRequest
import ecommerce.dto.LoginResponse
import ecommerce.dto.RegisterRequest
import ecommerce.dto.RegisterResponse
import ecommerce.service.JwtService
import ecommerce.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(
    val userService: UserService,
    val jwtService: JwtService,
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest,
    ): ResponseEntity<RegisterResponse> {
        val role = request.role ?: "USER"

        userService.register(request.email, request.password, role)
        val token = jwtService.generateToken(request.email)
        val response = RegisterResponse(token)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        val user =
            userService.getByEmail(request.email)
                ?: return ResponseEntity.status(403).build()

        val passwordMatches = userService.checkPassword(user, request.password)
        if (!passwordMatches) return ResponseEntity.status(403).build()

        val token = jwtService.generateToken(user.email)
        return ResponseEntity.ok(LoginResponse(token))
    }
}
