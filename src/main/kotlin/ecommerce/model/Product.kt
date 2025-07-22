package ecommerce.model

class Product(
    val id: Long? = null,
    var name: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
) {
    init {
        require(name.isNotBlank()) { "Product name must not be blank." }
        require(name.length <= 255) { "Product name must be 255 characters or fewer." }
        require(price >= 0) { "Product price must be non-negative." }
        require(imageUrl.length <= 255) { "Image URL must be 255 characters or fewer." }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
