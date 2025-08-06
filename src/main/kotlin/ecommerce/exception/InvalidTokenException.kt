package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidTokenException(message: String = "Token is not valid") : RuntimeException(message)
