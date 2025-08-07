package ecommerce.dto

import ecommerce.model.Product
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ProductRequestDTO(
    @field:NotNull(message = "Product name is required")
    @field:NotBlank(message = "Product name is required")
    @field:Size(min = 1, max = 15, message = "Name must have at maximum 15 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9()\\[\\]+\\-&/_ ]+$", message = "Invalid characters")
    var name: String,
    @field:NotNull(message = "Product price is required")
    @field:Positive(message = "Product price must be positive")
    var price: Double,
    @field:Pattern(
        regexp = "^https?://.*",
        message = "URL must start with http:// or https://",
    )
    @field:NotNull(message = "Product image URL is required")
    @field:NotBlank(message = "Product image URL is required")
    var imageUrl: String,
) {
    fun toEntity(): Product {
        return Product(
            name = name,
            price = price,
            imageUrl = imageUrl,
        )
    }
}
