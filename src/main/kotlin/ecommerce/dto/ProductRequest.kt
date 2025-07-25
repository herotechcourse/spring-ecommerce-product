package ecommerce.dto

import ecommerce.model.Product
import ecommerce.validation.ValidProductName
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ProductRequest(
    @field:NotBlank(message = "Product name must not be blank")
    @field:ValidProductName
    val name: String,
    @field:DecimalMin("0.01", message = "Price must be greater than 0")
    val price: Double,
    @field:Pattern(
        regexp = "^(http://|https://).*$",
        message = "Image URL must start with http:// or https://",
    )
    val imageUrl: String,
) {
    fun toProduct(): Product {
        return Product(name = name, price = price, imageUrl = imageUrl)
    }
}
