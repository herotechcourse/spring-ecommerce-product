package ecommerce.auth.controller

import ecommerce.auth.application.AuthService
import ecommerce.auth.infrastructure.AuthorizationExtractor
import ecommerce.auth.infrastructure.BearerAuthorizationExtractor
import ecommerce.auth.model.Member
import ecommerce.auth.model.MemberDTO
import ecommerce.auth.model.TokenRequest
import ecommerce.auth.model.TokenResponse
import ecommerce.auth.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/me/token")
    fun findMyInfo(request: HttpServletRequest): ResponseEntity<MemberDTO> {
        val token = authorizationExtractor.extract(request)
        val member = authService.findMemberByToken(token)
        return ResponseEntity.ok().body(member)
    }
}
