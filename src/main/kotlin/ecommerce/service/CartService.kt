package ecommerce.service

import ecommerce.dto.CartItemDto
import ecommerce.dto.CartItemRequest
import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import ecommerce.repository.CartRepository
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) {
    fun getAllItems(memberId: Long): CartItemDto {
        val cartId = cartRepository.findOrCreateCartId(memberId) ?: throw CartCreationException()
        val rawItems = cartRepository.showAllItemsInCart(cartId)
        val items = mutableListOf<Product>()
        rawItems.map { rawItem ->
             val product = productRepository.findById(rawItem.productId)
                ?: throw NotFoundException("Product with ${rawItem.productId} not found")
            items.add(product)
        }
        return CartItemDto(cartId = cartId, quantity = rawItem.quantity, items = items)
        // Can in `showAllItemsInCart` something go wrong? How can I prove it?
    }

    fun addItems(memberId: Long, request: CartItemRequest): CartItemDto {
        val cartId = cartRepository.findOrCreateCartId(memberId)
            ?: throw CartCreationException()
        repeat(request.quantity) {
            if (!cartRepository.addProductToCart(request.productId, cartId)) { throw ItemManipulationException("Product could not be added.") }
        }
        val quantity = cartRepository.quantityInCart(request.productId, cartId)
        val product = productRepository.findById(request.productId) ?: throw NotFoundException("Product with ${request.productId} not found")
        return CartItemDto(cartId, quantity, listOf(product))
    }

    fun deleteItems(memberId: Long, request: CartItemRequest) {
        val cartId = cartRepository.findOrCreateCartId(memberId) ?: throw CartCreationException()
        repeat(request.quantity) {
            if (!cartRepository.addProductToCart(request.productId, cartId)) { throw ItemManipulationException("Product could not be deleted.") }
        }
    }

}
