package ecommerce.service

import ecommerce.dto.cartItem.AddCartItemRequest
import ecommerce.dto.cartItem.CartItemResponse
import ecommerce.dto.cartItem.CartResponse
import ecommerce.exception.cartItem.CartItemNotFoundException
import ecommerce.exception.product.ProductNotFoundException
import ecommerce.model.CartItem
import ecommerce.model.Product
import ecommerce.repository.CartItemRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CartService(
    private val cartItemRepository: CartItemRepository,
    private val productService: ProductService,
) {
    fun addProductToCart(
        memberId: UUID,
        request: AddCartItemRequest,
    ): CartItemResponse {
        val product: Product
        try {
            product = productService.getProductById(request.productId)
            println("Product retrieved successfully: ${product.name}")
        } catch (e: ProductNotFoundException) {
            println("ERROR: ProductNotFoundException for productId: ${request.productId}. Message: ${e.message}")
            throw e
        } catch (e: Exception) {
            println("SEVERE ERROR: Unexpected exception while fetching product with ID ${request.productId}: ${e.message}")
            e.printStackTrace()
            throw e
        }
        val existingCartItem = cartItemRepository.findByMemberIdAndProductId(memberId, request.productId)
        val updatedCartItem: CartItem
        if (existingCartItem != null) {
            updatedCartItem = existingCartItem.copy(quantity = existingCartItem.quantity + request.quantity)
        } else {
            updatedCartItem =
                CartItem(
                    memberId = memberId,
                    productId = request.productId,
                    quantity = request.quantity,
                )
        }
        try {
            val savedCartItem = cartItemRepository.save(updatedCartItem)
            val response =
                CartItemResponse(
                    product.id,
                    product.name,
                    product.price,
                    product.img,
                    savedCartItem.quantity,
                )
            println("CartItemResponse created successfully for product: ${product.name}, quantity: ${savedCartItem.quantity}")
            return response
        } catch (e: Exception) {
            println("SEVERE ERROR: Unexpected exception while creating CartItemResponse for product ID ${product.id}: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    fun getCartItems(memberId: UUID): CartResponse {
        val cartItems = cartItemRepository.findAllByMemberId(memberId)
        val cartItemResponses =
            cartItems.mapNotNull { cartItem ->
                try {
                    val product = productService.getProductById(cartItem.productId)
                    CartItemResponse(
                        product.id,
                        product.name,
                        product.price,
                        product.img,
                        cartItem.quantity,
                    )
                } catch (e: ProductNotFoundException) {
                    println(
                        "Warning: Product with ID ${cartItem.productId} in cart for member $memberId not found in " +
                            "products table. Skipping this item from cart response.",
                    )
                    null
                }
            }
        val totalItems = cartItemResponses.sumOf { it.quantity }
        val totalPrice = cartItemResponses.sumOf { it.quantity * it.productPrice }
        return CartResponse(cartItemResponses, totalItems, totalPrice)
    }

    fun removeProductFromCart(
        memberId: UUID,
        productId: Long,
    ) {
        val cartItem =
            cartItemRepository.findByMemberIdAndProductId(memberId, productId) ?: throw CartItemNotFoundException(
                "Product with ID $productId not found in cart for member.",
            )
        cartItemRepository.deleteById(cartItem.id)
    }

    fun clearCart(memberId: UUID) {
        val cartItems = cartItemRepository.findAllByMemberId(memberId)
        cartItems.forEach { cartItemRepository.deleteById(it.id) }
    }
}
