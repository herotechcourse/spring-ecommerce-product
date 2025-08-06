package ecommerce.model

import java.sql.Timestamp

data class CartItem(
    var id: Long,
    var memberId: Long,
    var productId: Long,
    var quantity: Int,
    var createdAt: Timestamp,
    var updatedAt: Timestamp? = null,
)
