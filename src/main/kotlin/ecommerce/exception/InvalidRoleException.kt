package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InvalidRoleException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)
