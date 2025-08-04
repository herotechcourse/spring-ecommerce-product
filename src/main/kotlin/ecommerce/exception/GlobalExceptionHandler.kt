package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(ex: ProductNotFoundException): ResponseEntity<ErrorMessageModel> =
        buildError(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(
        NotFoundException::class,
    )
    fun handleInternalServerExceptions(ex: RuntimeException): ResponseEntity<ErrorMessageModel> =
        buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)

    @ExceptionHandler(
        ProductAlreadyExistsException::class,
        EmailAlreadyExistsException::class,
    )
    fun handleConflictExceptions(ex: RuntimeException): ResponseEntity<ErrorMessageModel> = buildError(HttpStatus.CONFLICT, ex.message)

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException): ResponseEntity<ErrorMessageModel> = buildError(HttpStatus.UNAUTHORIZED, ex.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(ex: MethodArgumentNotValidException): ResponseEntity<ErrorMessageModel> {
        val errors =
            ex.bindingResult.fieldErrors.associate {
                it.field to (it.defaultMessage ?: "Invalid value")
            }
        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", errors)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorMessageModel> =
        buildError(HttpStatus.BAD_REQUEST, ex.message)

    @ExceptionHandler(Exception::class)
    fun handleOtherExceptions(ex: Exception): ResponseEntity<ErrorMessageModel> =
        buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error: ${ex.message}")

    private fun buildError(
        status: HttpStatus,
        message: String?,
        details: Any? = null,
    ): ResponseEntity<ErrorMessageModel> {
        val body = ErrorMessageModel(status.value(), message ?: "No details found")
        return ResponseEntity(body, status)
    }
}
