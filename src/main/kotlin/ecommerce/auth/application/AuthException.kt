package ecommerce.auth.application

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class AuthException : RuntimeException()

@ResponseStatus(HttpStatus.CONFLICT)
class EmailAlreadyInUseException : RuntimeException()
