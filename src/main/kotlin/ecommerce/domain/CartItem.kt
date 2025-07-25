package ecommerce.domain

data class CartItem(var id: Long = 0, val cartId: Long, val productId: Long, var quantity: Int)
