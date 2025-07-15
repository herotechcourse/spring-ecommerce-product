package ecommerce

import java.net.URI

class Product(
    val id: Long? = null,
    val name: String = "",
    val price: Double = 0.0,
    val imageURL: URI
) {
    companion object {
        fun toEntity(product: Product, id: Long): Product {
            return Product(id, product.name, product.price, product.imageURL)
        }
    }
}
