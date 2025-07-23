package ecommerce.advice

import ecommerce.exception.InvalidInputException
import ecommerce.exception.ResourceNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<Map<String, String?>> {
        val errors = mutableMapOf<String, String?>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = if (error is FieldError) error.field else error.objectName
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage
        }
        println("Validation error for request ${request.requestURI}: $errors")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        ex: ResourceNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<String> {
        println("Resource not found for request ${request.requestURI}: ${ex.message}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: HttpServletRequest,
    ): ResponseEntity<String> {
        println("Illegal argument for request ${request.requestURI}: ${ex.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(InvalidInputException::class)
    fun handleInvalidInputException(
        ex: InvalidInputException,
        request: HttpServletRequest,
    ): ResponseEntity<String> {
        println("Invalid input for request ${request.requestURI}: ${ex.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException,
        request: HttpServletRequest,
    ): ResponseEntity<String> {
        val errorMessage =
            "Parameter '${ex.name}' has invalid value: '${ex.value}'. Expected type: ${ex.requiredType?.simpleName}."
        println("Type mismatch error for request ${request.requestURI}: $errorMessage")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingRequestParam(
        ex: MissingServletRequestParameterException,
        request: HttpServletRequest,
    ): ResponseEntity<String> {
        val errorMessage = "Required request parameter '${ex.parameterName}' is not present."
        println("Missing request parameter for request ${request.requestURI}: $errorMessage")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }

    @ExceptionHandler(MissingPathVariableException::class)
    fun handleMissingPathVariable(
        ex: MissingPathVariableException,
        request: HttpServletRequest,
    ): ResponseEntity<String> {
        val errorMessage = "Required path variable '${ex.variableName}' is not present."
        println("Missing path variable for request ${request.requestURI}: $errorMessage")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllOtherExceptions(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<String> {
        ex.printStackTrace()
        System.err.println("An unhandled exception occurred for request ${request.requestURI}: ${ex.message}")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred. Please try again later.")
    }
}
