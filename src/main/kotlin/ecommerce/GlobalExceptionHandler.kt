package ecommerce

import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.NotFoundException
import org.springframework.dao.DataAccessException
import org.springframework.http.ResponseEntity
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
    fun handleDataAccessException(e: Exception): ResponseEntity<Map<String, Any>> {
        val error = mapOf("message" to e.message)
        val errorBody = mapOf("errors" to error)
        println("DataAccessException occurred: $errorBody")
        return ResponseEntity.internalServerError().body(errorBody)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handlerIllegalStateException(e: Exception): ResponseEntity<Map<String, Any>> {
        val error = mapOf("message" to e.message)
        val errorBody = mapOf("errors" to error)
        println("IllegalStateException occurred: $errorBody")
        return ResponseEntity.internalServerError().body(errorBody)
    }
}
