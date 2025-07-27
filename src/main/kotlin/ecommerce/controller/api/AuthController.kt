package ecommerce.controller.api

import ecommerce.dto.AuthResponse
import ecommerce.dto.LoginForm
import ecommerce.dto.RegisterForm
import ecommerce.exception.MemberEmailAlreadyExistsException
import ecommerce.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/members")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun registerMember(
        @RequestBody @Valid form: RegisterForm,
    ): ResponseEntity<AuthResponse> {
        val member = authService.registerMember(form)
        val uri = URI.create("/api/members/${member.id}")
        val authResponse = authService.loginMember(LoginForm.fromRegisterForm(form))
        return ResponseEntity.created(uri).body(authResponse)
    }

    @PostMapping("/login")
    fun loginMember(
        @RequestBody @Valid form: LoginForm,
    ): ResponseEntity<AuthResponse> {
        val authResponse = authService.loginMember(form)
        return ResponseEntity.ok(authResponse)
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException::class)
    fun handleMemberEmailAlreadyExistsExceptionHandler(e: Exception): ResponseEntity<Map<String, Any>> {
        val error = mapOf("name" to e.message)
        val errorBody = mapOf("errors" to error)
        println("MemberEmailAlreadyExistsException occurred: $errorBody")
        return ResponseEntity.badRequest().body(errorBody)
    }

    companion object {
        val TEST_RESPONSE = AuthResponse(accessToken = "sampleToken")
    }
}
