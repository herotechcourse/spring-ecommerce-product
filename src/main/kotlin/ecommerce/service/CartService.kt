package ecommerce.service

import ecommerce.dto.CartRequest
import ecommerce.dto.CartResponse
import ecommerce.dto.mapper.CartMapper
import ecommerce.entity.CartItem
import ecommerce.exception.NotFoundException
import ecommerce.exception.RetrievalFailedException
import ecommerce.repository.CartRepository
import org.springframework.stereotype.Service

@Service
class CartService(private val repository: CartRepository) {
    fun findByMemberId(memberId: Long): List<CartItem> {
        return repository.selectByMemberId(memberId)
    }

    fun insertNewItemToCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): CartResponse {
        return insertNewItemsToCart(
            memberId,
            listOf(CartRequest(productId, quantity)),
        ).first()
    }

    fun insertNewItemsToCart(
        memberId: Long,
        requests: List<CartRequest>,
    ): List<CartResponse> {
        return requests.map { request ->
            val newItemId =
                repository.insertNewItem(CartMapper.toNewItem(memberId, request))
                    ?: throw RetrievalFailedException("Failed to insert cart")

            val cartItem =
                repository.findByItemId(newItemId)
                    ?: throw RetrievalFailedException("Inserted cart item not found")

            CartMapper.toResponse(cartItem)
        }
    }

    fun deleteBy(
        memberId: Long,
        productId: Long,
    ) {
        val deletedRows = repository.deleteByMemberIdAndProductId(memberId, productId)
        if (deletedRows == 0) {
            throw NotFoundException("No item found to delete for product $productId")
        }
    }
}
