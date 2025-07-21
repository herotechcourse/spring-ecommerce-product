package ecommerce

data class Product(
    var id: Long? = null,
    var name: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
) {
    override fun equals(other: Any?): Boolean {
        if (this == other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Product

        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: System.identityHashCode(this)
    }
}
