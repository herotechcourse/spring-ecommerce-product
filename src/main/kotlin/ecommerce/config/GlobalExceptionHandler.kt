package ecommerce.config

import ecommerce.exception.EmailAlreadyUsedException
import ecommerce.exception.InvalidRoleException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyUsedException::class)
    fun handleEmailAlreadyUsedException(ex: EmailAlreadyUsedException): ResponseEntity<Map<String, String>> {
        val body = mapOf("error" to (ex.message ?: "Email already in use"))
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidRoleException::class)
    fun handleInvalidRoleException(ex: InvalidRoleException): ResponseEntity<Map<String, String>> {
        val body = mapOf("error" to (ex.reason ?: "Invalid role provided"))
        return ResponseEntity(body, ex.statusCode)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors =
            ex.bindingResult
                .fieldErrors
                .associate { it.field to (it.defaultMessage ?: "Invalid value") }

        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<Map<String, String>> {
        val body = mapOf("error" to (ex.reason ?: "Unexpected error"))
        return ResponseEntity(body, ex.statusCode)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<Map<String, String>> {
        val body = mapOf("error" to "An unexpected error occurred")
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
