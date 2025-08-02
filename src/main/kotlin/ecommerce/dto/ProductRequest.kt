package ecommerce.dto

import ecommerce.entity.Product
import jakarta.validation.constraints.NotBlank

data class ProductRequest(
    @field:NotBlank
    val name: String,
    val price: Double,
    @field:NotBlank
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
