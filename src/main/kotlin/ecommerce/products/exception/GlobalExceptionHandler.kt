package ecommerce.products.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors =
            ex.bindingResult.fieldErrors.associate {
                it.field to (it.defaultMessage ?: "Invalid value")
            }
        return ResponseEntity.badRequest().body(mapOf("errors" to errors))
    }

    @ExceptionHandler(ProductValidationException::class)
    fun handleProductValidationException(ex: ProductValidationException): ResponseEntity<Map<String, Any>> {
        val errors = mapOf("name" to (ex.message ?: "Invalid product name"))
        return ResponseEntity.badRequest().body(mapOf("errors" to errors))
    }
}
