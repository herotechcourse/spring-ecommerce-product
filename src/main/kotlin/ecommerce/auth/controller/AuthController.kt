package ecommerce.auth.controller

import ecommerce.auth.extractor.BearerAuthorizationExtractor
import ecommerce.auth.service.AuthService
import ecommerce.member.dto.MemberResponse
import ecommerce.member.dto.TokenRequest
import ecommerce.member.dto.TokenResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class AuthController(
    private val authService: AuthService,
) {
    private val authorizationExtractor: BearerAuthorizationExtractor = BearerAuthorizationExtractor()

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody tokenRequest: TokenRequest,
    ): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.register(tokenRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody tokenRequest: TokenRequest,
    ): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.createToken(tokenRequest)
        return ResponseEntity.ok().body(tokenResponse)
    }

    @GetMapping("/me")
    fun findMyInfo(request: HttpServletRequest): ResponseEntity<MemberResponse> {
        val token = authorizationExtractor.extract(request)
        val member = authService.findMemberByToken(token)
        return ResponseEntity.ok().body(member)
    }
}
