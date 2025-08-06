package ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
class RetrievalFailedException(message: String) : RuntimeException(message)
