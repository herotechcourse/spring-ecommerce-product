package ecommerce.exception

class ErrorResponse(
    val error: String,
    val details: Map<String, String>? = null,
)
