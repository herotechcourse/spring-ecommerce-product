package ecommerce.model

import ecommerce.repository.ProductResponse

data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
)

fun Product.toDto(): ProductResponse {
    return ProductResponse(id, name, price, imageUrl)
}
