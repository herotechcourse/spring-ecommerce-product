package ecommerce.advice

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResult(ex: EmptyResultDataAccessException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Resource not found: ${ex.message}")
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        val rootCause = ex.cause

        val message =
            when (rootCause) {
                is MismatchedInputException -> {
                    val fieldName = rootCause.path?.firstOrNull()?.fieldName ?: "unknown"
                    "Missing or invalid value for field: '$fieldName'"
                }
                else -> "Invalid request payload"
            }
        return ResponseEntity(message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<String> {
        val rootCause = ex.rootCause?.message ?: ex.message ?: "Data integrity violation"

        val message =
            if (rootCause.contains("unique", ignoreCase = true) ||
                rootCause.contains("constraint", ignoreCase = true)
            ) {
                "Duplicate product name is not allowed"
            } else {
                "Database constraint violation"
            }

        return ResponseEntity(message, HttpStatus.BAD_REQUEST)
    }
}
