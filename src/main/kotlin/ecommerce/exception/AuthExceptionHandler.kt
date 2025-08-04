package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class AuthException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.CONFLICT)
class EmailAlreadyInUseException(message: String) : RuntimeException(message)

@RestControllerAdvice("authExceptionHandler")
class GlobalExceptionHandler {
    @ExceptionHandler(AuthException::class)
    fun handleInvalidAuthException(e: AuthException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(e.message ?: "Authentication failed")
    }

    @ExceptionHandler(EmailAlreadyInUseException::class)
    fun handleEmailAlreadyInUseException(e: EmailAlreadyInUseException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(e.message ?: "Email already in use")
    }
}
