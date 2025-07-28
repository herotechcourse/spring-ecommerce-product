package ecommerce.dto.mapper

import ecommerce.dto.CartResponse
import ecommerce.entity.CartItem

object CartMapper {
    fun toResponse(item: CartItem): CartResponse {
        return CartResponse(
            item.id,
            item.productId,
            item.quantity,
            item.updatedAt,
        )
    }
}
