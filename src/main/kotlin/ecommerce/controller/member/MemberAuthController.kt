package ecommerce.controller.member

import ecommerce.dto.auth.LoginRequest
import ecommerce.dto.user.UserRequestDTO
import ecommerce.service.MemberAuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member/auth")
class MemberAuthController(
    val memberAuthService: MemberAuthService,
) {
    @PostMapping("/signUp")
    fun signUp(
        @RequestBody @Valid userDTO: UserRequestDTO,
    ): ResponseEntity<Map<String, String>> {
        val userCreateResponse = memberAuthService.signUp(userDTO)
        return ResponseEntity.created(userCreateResponse.uri)
            .body(mapOf("token" to userCreateResponse.token))
    }

    @PostMapping("/signIn")
    fun signIn(
        @RequestBody @Valid loginRequest: LoginRequest,
    ): ResponseEntity<Map<String, String>> {
        val token = memberAuthService.logIn(loginRequest)
        return ResponseEntity.ok().body(mapOf("token" to token))
    }
}
