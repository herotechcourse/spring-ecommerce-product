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
    ): ResponseEntity<Void> {
        val userCreateResponse = memberAuthService.signUp(userDTO)
        return ResponseEntity.created(userCreateResponse.uri)
            .header("Authorization", userCreateResponse.token)
            .build()
    }

    @PostMapping("/signIn")
    fun signIn(
        @RequestBody @Valid loginRequest: LoginRequest,
    ): ResponseEntity<Void> {
        val token = memberAuthService.logIn(loginRequest)
        return ResponseEntity.ok().header("Authorization", token).build()
    }
}
