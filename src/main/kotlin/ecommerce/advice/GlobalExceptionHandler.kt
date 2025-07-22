package ecommerce.advice

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import ecommerce.dto.ErrorResponse
import ecommerce.exception.DuplicateProductNameException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResult(request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        return errorResponse(HttpStatus.NOT_FOUND, "Resource not found", request)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val rootCause = ex.cause
        val message =
            when (rootCause) {
                is com.fasterxml.jackson.databind.exc.MismatchedInputException -> {
                    val fieldName = rootCause.path?.firstOrNull()?.fieldName ?: "unknown"
                    "Missing or invalid value for field: '$fieldName'"
                }
                else -> "Invalid request payload"
            }
        return errorResponse(HttpStatus.BAD_REQUEST, message, request)
    }

    @ExceptionHandler(DuplicateProductNameException::class)
    fun handleDuplicateProductName(
        ex: DuplicateProductNameException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return errorResponse(HttpStatus.CONFLICT, ex.message ?: "Duplicate product", request)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        return errorResponse(HttpStatus.BAD_REQUEST, errors, request)
    }

    private fun errorResponse(
        status: HttpStatus,
        message: Any,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val error = status.reasonPhrase
        val response =
            ErrorResponse(
                status = status.value(),
                error = error,
                message = message,
                path = request.requestURI,
            )
        return ResponseEntity.status(status).body(response)
    }
}
