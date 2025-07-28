package ecommerce.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ProductRequest(
    @field:NotBlank(message = "Name must not be blank")
    @field:Size(max = 15, message = "Name must be at most 15 characters long")
    @field:Pattern(
        regexp = "^[\\w\\s()\\[\\]+\\-&/_]*$",
        message = "Invalid characters in name. Allowed: letters, numbers, spaces, (, ), [, ], +, -, &, /, _",
    )
    val name: String,
    @field:Positive(message = "Price must be greater than 0")
    val price: Double,
    @field:NotBlank(message = "Image URL must not be blank")
    @field:Pattern(
        regexp = "^(https?|ftp)://[\\w.-]+(?:\\.[\\w.-]+)+[/#?]?.*\$",
        message = "Invalid URL format, example: https://example.com/image.jpg",
    )
    val imageUrl: String,
)
