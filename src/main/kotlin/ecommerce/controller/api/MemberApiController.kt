package ecommerce.controller.api

import ecommerce.dto.member.AuthResponse
import ecommerce.dto.member.MemberLoginRequest
import ecommerce.dto.member.MemberRegisterRequest
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
class MemberApiController(private val authService: AuthService) {
    @PostMapping("/register")
    fun register(
        @Valid
        @RequestBody
        request: MemberRegisterRequest,
    ): ResponseEntity<AuthResponse> {
        val token =
            authService.registerMember(
                userName = request.userName,
                email = request.email,
                password = request.password,
                role = "USER",
            )

        val authResponse =
            AuthResponse(
                success = true,
                message = "Successfully registered",
                token = token,
                userName = request.userName,
            )
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse)
    }

    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody
        request: MemberLoginRequest,
    ): ResponseEntity<AuthResponse> {
        val (member, accessToken) =
            authService.loginMember(
                email = request.email,
                password = request.password,
            )
        val authResponse =
            AuthResponse(
                success = true,
                message = "Successfully logged in",
                token = accessToken,
                userName = member.userName,
            )
        return ResponseEntity.ok().body(authResponse)
    }
}
