package ecommerce.service

import ecommerce.dto.cart.CartDTO
import ecommerce.dto.cartProduct.CartProductDTO
import ecommerce.exception.EntityNotFoundException
import ecommerce.repository.CartProductRepository
import ecommerce.repository.CartRepository
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartProductRepository: CartProductRepository,
    private val productRepository: ProductRepository,
) {
    fun getCartProducts(userID: Long?): List<CartProductDTO> {
        val cart = getUserCart(userID)
        return cartProductRepository.getCartProducts(cart.id)
    }

    fun addProductToCart(
        userID: Long?,
        productID: Long,
    ) {
        val cart = getUserCart(userID)
        checkValidProduct(productID)

        val cartProduct = cartProductRepository.findCartProduct(cart.id, productID)

        if (cartProduct == null) {
            cartProductRepository.addProduct(cart.id, productID)
        } else {
            cartProductRepository.updateProductQuantity(cartProduct.id, cartProduct.quantity + 1)
        }
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
    }

    private fun getUserCart(userID: Long?): CartDTO {
        return cartRepository.findMembersCart(userID)
            ?: throw EntityNotFoundException("Cart not found")
    }

    private fun checkValidProduct(productID: Long) {
        productRepository.findById(productID) ?: throw EntityNotFoundException("Product not found")
    }
}
