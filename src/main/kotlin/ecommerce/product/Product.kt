package ecommerce.product

import java.math.BigDecimal

class Product(
    var id: Long? = null,
    var name: String,
    var price: BigDecimal,
    var imageUrl: String?
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(name.length <= 255) { "Name must be at most 255 characters" }
        require(price > BigDecimal.ZERO) { "Price must be positive" }
    }

    fun updateWith(partial: Product): Product {
        return Product(
            name = partial.name ?: this.name,
            price = partial.price ?: this.price,
            imageUrl = partial.imageUrl ?: this.imageUrl
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        fun toEntity(
            product: Product,
            id: Long,
        ): Product {
            return Product(id, product.name, product.price, product.imageUrl)
        }
    }
}