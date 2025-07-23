package ecommerce.controller

import ecommerce.dto.RegistrationRequest
import ecommerce.dto.TokenResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class AuthenticationController {
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
    fun signUp(
        @RequestBody request: RegistrationRequest,
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(TokenResponse("token_example"))
    }
}
