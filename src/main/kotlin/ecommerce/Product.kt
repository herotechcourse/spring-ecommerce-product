package ecommerce

class Product(
    var id: Long? = null,
    var name: String,
    var price: Double,
    var imageUrl: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
