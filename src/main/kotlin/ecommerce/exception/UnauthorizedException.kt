package ecommerce.exception

class UnauthorizedException(message: String = "Authentication required or invalid token.") : RuntimeException(message)
