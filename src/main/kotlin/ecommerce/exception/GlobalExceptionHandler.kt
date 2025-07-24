package ecommerce.exception

import ecommerce.auth.exception.AuthorizationException
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationException::class)
    fun handleAuthorizationException(e: AuthorizationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to "Unauthorized: ${e.message}"), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(e: ValidationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to e.message.orEmpty()), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            when (error) {
                is FieldError -> {
                    val fieldName = error.field
                    val errorMessage = error.defaultMessage ?: "Invalid value"
                    errors[fieldName] = errorMessage
                }
                is ObjectError -> {
                    val errorMessage = error.defaultMessage ?: "Validation error"
                    errors[error.objectName] = errorMessage
                }
            }
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }
}
