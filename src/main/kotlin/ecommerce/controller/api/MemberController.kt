package ecommerce.controller.api

import ecommerce.service.AuthService
import ecommerce.application.AuthorizationExtractor
import ecommerce.application.BearerAuthorizationExtractor
import ecommerce.model.Member
import ecommerce.dto.MemberDTO
import ecommerce.dto.TokenRequest
import ecommerce.dto.TokenResponse
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
        @Valid @RequestBody member: Member,
    ): ResponseEntity<TokenResponse> {
        val created = userService.create(member)
        val dto = MemberDTO.from(created)
        val tokenRequest = TokenRequest(member.email, member.password)
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
