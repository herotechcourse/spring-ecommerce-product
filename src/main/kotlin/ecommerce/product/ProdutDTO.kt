package ecommerce.product

import java.math.BigDecimal

data class ProductDTO(
    val name: String?,
    val price: BigDecimal?,
    val imageUrl: String?
)

fun ProductDTO.toProduct(): Product {
    requireNotNull(name) { "Name is required" }
    requireNotNull(price) { "Price is required" }
    return Product(name = name, price = price, imageUrl = imageUrl)
}
