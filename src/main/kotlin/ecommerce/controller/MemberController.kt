package ecommerce.controller

import ecommerce.dto.MemberRequest
import ecommerce.dto.TokenResponse
import ecommerce.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val service: MemberService,
) {
    @PostMapping(REGISTER_ENDPOINT)
    fun register(
        @Valid @RequestBody request: MemberRequest,
    ): ResponseEntity<TokenResponse> {
        val response = service.registerByEmail(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping(LOGIN_ENDPOINT)
    fun login(
        @Valid @RequestBody request: MemberRequest,
    ): ResponseEntity<TokenResponse> {
        val response = service.loginByEmail(request)
        return ResponseEntity.ok(response)
    }

    companion object {
        const val REGISTER_ENDPOINT = "/api/members/register"
        const val LOGIN_ENDPOINT = "/api/members/login"
    }
}
