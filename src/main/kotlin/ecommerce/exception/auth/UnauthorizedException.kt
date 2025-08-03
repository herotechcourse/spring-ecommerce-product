package ecommerce.exception.auth

class UnauthorizedException(message: String = "Authentication required or invalid token.") : RuntimeException(message)
