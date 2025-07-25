package ecommerce.exception

class ForbiddenException(message: String = "Access to this resource is forbidden.") : RuntimeException(message)
