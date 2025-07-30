package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException::class)
    fun handleUserNotFound(ex: ProductNotFoundException): ResponseEntity<ErrorMessageModel> {
        val errorMessage =
            ErrorMessageModel(
                HttpStatus.NOT_FOUND.value(),
                ex.message,
            )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRunTime(ex: RuntimeException): ResponseEntity<ErrorMessageModel> {
        val errorMessage =
            ErrorMessageModel(
                HttpStatus.BAD_REQUEST.value(),
                ex.message,
            )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(
        value = [
            ProductCreationException::class,
            ProductUpdateException::class,
            ElementNotFoundException::class,
        ],
    )
    fun handleUserNotFound(ex: RuntimeException): ResponseEntity<ErrorMessageModel> {
        val errorMessage =
            ErrorMessageModel(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.message,
            )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors =
            ex.bindingResult.fieldErrors.associate {
                it.field to (it.defaultMessage ?: "Invalid value")
            }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(
        value = [
            ProductAlreadyInDBException::class,
            MemberEmailAlreadyExistsException::class,
        ],
    )
    fun handleProductAlreadyInDBException(ex: RuntimeException): ResponseEntity<ErrorMessageModel> {
        val errorMessage =
            ErrorMessageModel(
                HttpStatus.CONFLICT.value(),
                ex.message,
            )
        return ResponseEntity(errorMessage, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(
        value = [
            EmailOrPasswordIncorrectException::class,
            ForbiddenException::class,
        ],
    )
    fun handleEmailOrPasswordIncorrectException(ex: RuntimeException): ResponseEntity<ErrorMessageModel> {
        val errorMessage =
            ErrorMessageModel(
                HttpStatus.FORBIDDEN.value(),
                ex.message,
            )
        return ResponseEntity(errorMessage, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<ErrorMessageModel> {
        val errorMessage =
            ErrorMessageModel(
                HttpStatus.UNAUTHORIZED.value(),
                ex.message,
            )
        return ResponseEntity(errorMessage, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorMessageModel> {
        val errorMessage =
            ErrorMessageModel(
                HttpStatus.BAD_REQUEST.value(),
                ex.message,
            )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }
}
