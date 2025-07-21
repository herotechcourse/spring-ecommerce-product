package ecommerce.model

class Product(
    val id: Long? = null,
    var name: String = "",
    var price: Double = 0.0,
    var imageUrl: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
