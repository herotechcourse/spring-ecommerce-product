package ecommerce.controller

import ecommerce.auth.AuthService
import ecommerce.model.TokenRequest
import ecommerce.model.TokenResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/members")
class TokenLoginController(
    private val authService: AuthService,
    ) {
    @PostMapping("/register")
    fun registerMember(@RequestBody request: TokenRequest): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.register(request)
        return ResponseEntity.ok(tokenResponse)
    }

    @PostMapping("/login")
    fun loginMember(@RequestBody request: TokenRequest): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.login(request)
        return ResponseEntity.ok(tokenResponse)
    }

//    /**
//     * ex) request sample
//     *
//     * GET /api/members/me/token HTTP/1.1
//     * authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MTAzNzY2NzIsImV4cCI6MTYxMDM4MDI3Mn0.Gy4g5RwK1Nr7bKT1TOFS4Da6wxWh8l97gmMQDgF8c1E
//     * accept: application/json
//     */
//    @GetMapping("me/token")
//    fun findMyInfo(request: HttpServletRequest): ResponseEntity<MemberResponse> {
//        val token = authorizationExtractor.extract(request)
//        val member = authService.findMemberByToken(token)
//        return ResponseEntity.ok().body(member)
//    }

}
