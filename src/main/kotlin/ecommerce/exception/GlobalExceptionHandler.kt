package ecommerce.exception

import ecommerce.exception.auth.EmailAlreadyExistsException
import ecommerce.exception.auth.InvalidCredentialsException
import ecommerce.exception.cartItem.CartItemNotFoundException
import ecommerce.exception.product.DuplicateProductNameException
import ecommerce.exception.product.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String?, Any>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid Value") }
        return ResponseEntity.badRequest().body(mapOf("errors" to errors))
    }

    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleProductNotFound(ex: ProductNotFoundException): Map<String, String> {
        return mapOf("error" to ex.message.orEmpty())
    }

    @ExceptionHandler(DuplicateProductNameException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDuplicateException(ex: DuplicateProductNameException): Map<String, String> {
        return mapOf("error" to ex.message.orEmpty())
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(ex: Exception): Map<String, String> {
        return mapOf("error" to "An unexpected error occurred")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): Map<String, String> {
        return mapOf("error" to ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailAlreadyExistsException(ex: EmailAlreadyExistsException): Map<String, String> {
        return mapOf("error" to ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentialsException(ex: InvalidCredentialsException): Map<String, String> {
        return mapOf("error" to ex.message.orEmpty())
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CartItemNotFoundException::class)
    fun handleCartItemNotFoundException(ex: CartItemNotFoundException): Map<String, String> {
        return mapOf("error" to ex.message.orEmpty())
    }
}
