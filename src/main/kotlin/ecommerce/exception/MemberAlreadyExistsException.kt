package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class MemberAlreadyExistsException(message: String) : RuntimeException(message)
