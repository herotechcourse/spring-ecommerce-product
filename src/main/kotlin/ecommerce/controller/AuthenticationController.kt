package ecommerce.controller

import ecommerce.dto.RegistrationRequest
import ecommerce.dto.TokenResponse
import ecommerce.service.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class AuthenticationController(private val authenticationService: AuthenticationService) {
    /**
     * ex) request sample
     *
     POST /api/members/register HTTP/1.1
     Content-Type: application/json
     host: localhost:8080

     {
     "email": "admin@email.com",
     "password": "password"
     }
     */
    @PostMapping("/register")
    fun registerMember(
        @RequestBody request: RegistrationRequest,
    ): ResponseEntity<TokenResponse> {
        val token = authenticationService.registerMember(request)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: RegistrationRequest,
    ): ResponseEntity<TokenResponse> {
        val token = authenticationService.logIn(request)
        return ResponseEntity.ok(token)
    }
}
