package ecommerce.controller.api

import ecommerce.application.AuthorizationExtractor
import ecommerce.application.BearerAuthorizationExtractor
import ecommerce.dto.MemberDTO
import ecommerce.dto.MemberRequestDTO
import ecommerce.dto.TokenRequest
import ecommerce.dto.TokenResponse
import ecommerce.service.AuthService
import ecommerce.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val userService: MemberService,
    private val authService: AuthService,
) {
    private val authorizationExtractor: AuthorizationExtractor<String> = BearerAuthorizationExtractor()

    @PostMapping("/register")
    fun registerUser(
        @Valid @RequestBody memberRequest: MemberRequestDTO,
    ): ResponseEntity<TokenResponse> {
        val created = userService.create(memberRequest)
        val dto = MemberDTO.from(created)
        val tokenRequest = TokenRequest(created.email, created.password)
        val tokenResponse = authService.createToken(tokenRequest)
        return ResponseEntity.created(URI("/users/${dto.id}")).body(tokenResponse)
    }

    @PostMapping("/login")
    fun loginUser(
        @RequestBody tokenRequest: TokenRequest,
    ): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.issueTokenToLoggedUSer(tokenRequest)
        return ResponseEntity.ok().body(tokenResponse)
    }
}
