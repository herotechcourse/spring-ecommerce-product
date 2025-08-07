package ecommerce.dto.product

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ProductRequest(
    @field:NotBlank(message = "Product name cannot be blank")
    @field:Size(max = 15, message = "Product name must be shorter than 15 characters")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]*$",
        message = "Product name contains invalid characters",
    )
    val name: String,
    @field:DecimalMin(value = "0.01", message = "Product price must be greater than 0.01")
    val price: Double,
    @field:NotBlank(message = "Product image URL cannot be blank")
    @field:Pattern(
        regexp = "^https?://.*",
        message = "Product image URL must start with http:// or https://",
    )
    val imageUrl: String,
)
