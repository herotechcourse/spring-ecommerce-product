package ecommerce.model

import ecommerce.dto.ProductRequest

class Product(
    val name: String,
    val price: Double,
    val imageUrl: String,
    var id: Long? = null,
) {
    init {
        require(name.isNotBlank())
        require(Regex("""^[\w\s()\[\]+\-&/_]*$""").matches(name))
        require(name.length < 15)
        require(price > 0)
        require(Regex("""^(http://|https://).+""").matches(imageUrl))
    }

    companion object {
        fun toEntity(product: ProductRequest): Product {
            return Product(
                product.name,
                product.price,
                product.imageUrl,
                product.id,
            )
        }
    }
}
