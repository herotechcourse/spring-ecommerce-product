package ecommerce.domain

class NewCartItem(
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
) {
    init {
        require(quantity > 0) { "quantity must be positive" }
    }
}
