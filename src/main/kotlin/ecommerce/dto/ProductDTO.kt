package ecommerce.dto

import ecommerce.model.Product

data class ProductDTO(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
) {
    companion object {
        fun from(product: Product): ProductDTO {
            return ProductDTO(
                id = product.id ?: 0,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
            )
        }
    }
}
