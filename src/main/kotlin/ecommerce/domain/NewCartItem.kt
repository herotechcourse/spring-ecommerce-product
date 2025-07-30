package ecommerce.domain

class NewCartItem(
    val memberId: Long,
    val productId: Long,
    val quantity: Int,
) {
    init {
        // TODO: logic
    }
}
