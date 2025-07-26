package ecommerce

import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.NotFoundException
import org.springframework.dao.DataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<Void> {
        println("NotFoundException occurred: " + e.message)
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(InternalServerErrorException::class)
    fun handleInternalServerErrorException(e: InternalServerErrorException): ResponseEntity<Void> {
        println("InternalServerErrorException occurred: " + e.message)
        return ResponseEntity.internalServerError().build()
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(e: Exception): ResponseEntity<Void> {
        println("DataAccessException occurred: " + e.message)
        return ResponseEntity.internalServerError().build()
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handlerIllegalStateException(e: Exception): ResponseEntity<Void> {
        println("IllegalStateException occurred: " + e.message)
        return ResponseEntity.internalServerError().build()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors =
            e.bindingResult.fieldErrors.associate { error ->
                error.field to (error.defaultMessage ?: "Invalid value")
            }
        val errorBody = mapOf("errors" to errors)
        println("MethodArgumentNotValidException occurred: $errorBody")
        return ResponseEntity.badRequest().body(errorBody)
    }
}
