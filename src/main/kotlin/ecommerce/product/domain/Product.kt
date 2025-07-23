package ecommerce.product.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class Product(
    var id: Long? = null,
    @field:NotBlank(message = "Product name cannot be blank")
    @field:Size(max = 15, message = "Product name must be 15 characters or less")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9 \\(\\)\\[\\]\\+\\-&/_]+$",
        message = "Product name can only contain letters, numbers, spaces, and allowed special characters: (), [], +, -, &, /, _",
    )
    var name: String = "",
    @field:Positive(message = "Price must be greater than 0")
    var price: Double = 0.00,
    @field:NotBlank(message = "Image URL cannot be blank")
    @field:Pattern(
        regexp = "^https?://.*",
        message = "Image URL must start with http:// or https://",
    )
    var imageUrl: String = "",
)