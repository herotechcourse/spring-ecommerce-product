package ecommerce.model

import ecommerce.dto.ProductResponse

class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (price != other.price) return false
        if (name != other.name) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}

fun Product.toDto(): ProductResponse {
    return ProductResponse(id, name, price, imageUrl)
}
