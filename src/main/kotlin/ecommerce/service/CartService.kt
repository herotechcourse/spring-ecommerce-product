package ecommerce.service

import ecommerce.dto.AllCartItemsDto
import ecommerce.dto.CartItemDto
import ecommerce.dto.CartItemRequest
import ecommerce.exception.NotFoundException
import ecommerce.repository.CartRepository
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) {
    fun getAllItems(memberId: Long): AllCartItemsDto {
        val cartId = cartRepository.findOrCreateCartId(memberId)
        val items = cartRepository.showAllItemsInCart(cartId)
        return AllCartItemsDto(cartId, items)
    }

    fun addItem(
        memberId: Long,
        request: CartItemRequest,
    ): CartItemDto {
        val cartId = cartRepository.findOrCreateCartId(memberId)
        if (!productRepository.existsById(request.productId)) {
            throw NotFoundException("Product with ${request.productId} not found")
        }
        return cartRepository.addItemToCart(request.productId, request.quantity, cartId)
    }

    fun deleteItem(
        memberId: Long,
        request: CartItemRequest,
    ): CartItemDto? {
        val cartId = cartRepository.findOrCreateCartId(memberId)
        if (!productRepository.existsById(request.productId)) {
            throw NotFoundException("Product with ${request.productId} not found")
        }
        return cartRepository.removeItemFromCart(request.productId, request.quantity, cartId)
    }
}
