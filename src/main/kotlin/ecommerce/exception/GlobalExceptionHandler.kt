package ecommerce.exception

import ecommerce.auth.exception.AuthorizationException
import ecommerce.auth.exception.DuplicateMemberEmailException
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthorizationException(e: AuthorizationException): ErrorResponse {
        return ErrorResponse(error = "Unauthorized: ${e.message}")
    }

    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleForbiddenException(e: ForbiddenException): ErrorResponse {
        return ErrorResponse(error = e.message.orEmpty())
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(e: ValidationException): ErrorResponse {
        return ErrorResponse(error = e.message.orEmpty())
    }

    @ExceptionHandler(DuplicateMemberEmailException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDuplicateMemberEmailException(e: DuplicateMemberEmailException): ErrorResponse {
        return ErrorResponse(error = e.message.orEmpty())
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ErrorResponse {
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
        return ErrorResponse(error = "Validation failed", details = errors)
    }
}
