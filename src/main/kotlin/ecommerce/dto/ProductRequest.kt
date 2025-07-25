package ecommerce.dto

import ecommerce.entity.Product
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ProductRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 15, message = "Name must not exceed 15 characters.")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]*$",
        message = "Name contains invalid special characters.",
    )
    val name: String,
    @field:DecimalMin(value = "0.01", message = "Price must be greater than 0.")
    val price: Double,
    @field:NotBlank
    @field:Pattern(
        regexp = "^https?://.*",
        message = "Image URL must start with http:// or https://",
    )
    val imageUrl: String,
) {
    fun toEntity(id: Long? = null): Product {
        return Product(
            id = id,
            name = this.name,
            price = this.price,
            imageUrl = this.imageUrl,
        )
    }
}
