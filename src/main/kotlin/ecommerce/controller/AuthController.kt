package ecommerce.controller

import ecommerce.service.AuthService
import ecommerce.dto.MemberDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController (
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: MemberDTO): ResponseEntity<Map<String, String>> {
        val token = authService.login(request)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @GetMapping("/members")
    fun secure(@RequestHeader("Authorization") authHeader: String): ResponseEntity<String> {
        return try {
            val email = authService.extractAndValidateToken(authHeader)
            ResponseEntity.ok("Hello, $email! Here are your products...")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(401).body("Invalid token. $e")
        }
    }
}
