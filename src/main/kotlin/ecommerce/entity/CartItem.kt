package ecommerce.entity

class CartItem(
    var id: Long? = null,
    var userId: Long,
    var productId: Long,
    var quantity: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CartItem

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
