package ecommerce.dto.product

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateProductRequest(
    @field:NotBlank(message = "Product name cannot be blank")
    @field:Size(max = 15, message = "Product name must be no more than 15 characters.")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9 \\(\\)\\[\\]\\+\\-/&_]*$",
        message = "Product name contains disallowed special characters. Allowed: ( ), [ ], +, -, &, /, _",
    )
    val name: String,
    @field:NotNull(message = "Product price can't be empty")
    @field:DecimalMin("0.01", message = "Product price must be greater than 0.")
    val price: Double,
    @field:NotBlank(message = "Image URL cannot be blank.")
    @field:Pattern(
        regexp = "^(http|https)://.*$",
        message = "Image URL must start with: http:// or https://",
    )
    val img: String,
    @field:NotNull(message = "Product quantity cannot be null")
    @field:Min(value = 1, message = "Product quantity must be greater than 0.")
    val quantity: Int,
)
