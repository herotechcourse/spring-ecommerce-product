package ecommerce.service

import ecommerce.dto.cart.CartDTO
import ecommerce.dto.cartProduct.CartProductResponseDTO
import ecommerce.enums.CartAction
import ecommerce.exception.EntityNotFoundException
import ecommerce.repository.CartProductRepository
import ecommerce.repository.CartRepository
import ecommerce.repository.CartStatisticsRepository
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartProductRepository: CartProductRepository,
    private val productRepository: ProductRepository,
    private val cartStatisticsRepository: CartStatisticsRepository,
) {
    fun getCartProducts(userID: Long?): List<CartProductResponseDTO> {
        val cart = getUserCart(userID)
        return cartProductRepository.getCartProducts(cart.id)
    }

    fun addProductToCart(
        userID: Long?,
        productID: Long,
    ): Long {
        var id: Long
        val cart = getUserCart(userID)
        checkValidProduct(productID)

        val cartProduct = cartProductRepository.findCartProduct(cart.id, productID)

        if (cartProduct == null) {
            id = cartProductRepository.addProduct(cart.id, productID)
        } else {
            id = cartProduct.id
            cartProductRepository.updateProductQuantity(cartProduct.id, cartProduct.quantity + 1)
        }
        cartStatisticsRepository.create(userID, productID, CartAction.ADD)
        return id
    }

    fun removeProductFromCart(
        userID: Long?,
        productID: Long,
    ) {
        val cart = getUserCart(userID)
        checkValidProduct(productID)

        val cartProduct =
            cartProductRepository.findCartProduct(cart.id, productID)
                ?: throw EntityNotFoundException("Product not in cart")

        val rows =
            if (cartProduct.quantity == 1) {
                cartProductRepository.removeProduct(cart.id, productID)
            } else {
                cartProductRepository.updateProductQuantity(cartProduct.id, cartProduct.quantity - 1)
            }
        cartStatisticsRepository.create(userID, productID, CartAction.DELETE)
    }

    private fun getUserCart(userID: Long?): CartDTO {
        return cartRepository.findMembersCart(userID)
            ?: throw EntityNotFoundException("Cart not found")
    }

    private fun checkValidProduct(productID: Long) {
        productRepository.findById(productID) ?: throw EntityNotFoundException("Product not found")
    }
}
