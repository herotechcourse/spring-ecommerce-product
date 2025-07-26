package ecommerce.dto

class CartItemRequest(
    // Todo validate: id and quantity have to be strictly positive
    val productId: Long,
    val quantity: Long,
)
