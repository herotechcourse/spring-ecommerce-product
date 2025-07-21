package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleUserNotFound(ex: NoSuchElementException): ResponseEntity<String?> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }
}
