package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @ExceptionHandler(ProductUpdateException::class)
    fun handleUpdateException(ex: ProductUpdateException): ResponseEntity<ErrorMessageModel> {
        val errorMessage =
            ErrorMessageModel(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.message,
            )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
