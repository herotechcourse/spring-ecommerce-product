package ecommerce

import java.net.URI

class Product(
    var id: Long? = null,
    var name: String = "",
    var price: Double = 0.0,
    var imageURL: URI
) {
    fun update(newProduct: Product) {
        this.name = newProduct.name
        this.price = newProduct.price
        this.imageURL = newProduct.imageURL
    }

    companion object {
        fun toEntity(product: Product, id: Long): Product {
            return Product(id, product.name, product.price, product.imageURL)
        }
    }
}
