package ecommerce

import ecommerce.exception.NotFoundException
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

    @ExceptionHandler(InterruptedException::class)
    fun handleInternalServerErrorException(e: InterruptedException): ResponseEntity<Void> {
        println("InterruptedException occurred: " + e.message)
        return ResponseEntity.internalServerError().build()
    }
}
