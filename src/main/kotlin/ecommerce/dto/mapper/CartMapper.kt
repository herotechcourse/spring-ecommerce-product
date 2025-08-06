package ecommerce.dto.mapper

import ecommerce.domain.NewCartItem
import ecommerce.dto.CartRequest
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

    fun toNewItem(
        memberId: Long,
        request: CartRequest,
    ): NewCartItem {
        return NewCartItem(
            memberId = memberId,
            productId = request.productId,
            quantity = request.quantity,
        )
    }
}
