package ecommerce


import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException

@RestController
@RequestMapping("/api/members")
class AuthController(
    private val authService: AuthService
) {
    private val authorizationExtractor: AuthorizationExtractor<String> = BearerAuthorizationExtractor()

    @PostMapping("/register")
    fun register(@Valid @RequestBody tokenRequest: TokenRequest): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.register(tokenRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody tokenRequest: TokenRequest): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.createToken(tokenRequest)
        return ResponseEntity.ok().body(tokenResponse)
    }

    @GetMapping("/me")
    fun findMyInfo(request: HttpServletRequest): ResponseEntity<MemberResponse> {
        val token = authorizationExtractor.extract(request)
        val member = authService.findMemberByToken(token)
        return ResponseEntity.ok().body(member)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to (ex.message ?: "Validation error")), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Invalid value"
            errors[fieldName] = errorMessage
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AuthorizationException::class)
    fun handleAuthorizationException(ex: AuthorizationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to "Unauthorized"), HttpStatus.UNAUTHORIZED)
    }
}