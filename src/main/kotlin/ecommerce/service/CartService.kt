package ecommerce.service

import ecommerce.dao.JdbcCartDao
import ecommerce.dao.JdbcProductDao
import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.NotFoundException
import ecommerce.model.CartItem
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CartService(
    private val jdbcCartDao: JdbcCartDao,
    private val jdbcProductDao: JdbcProductDao,
) {
    fun getCart(memberId: Long): List<CartItem> {
        return jdbcCartDao.getCartItemsByMemberId(memberId)
    }

    fun addToCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): ResponseEntity<String> {
        jdbcProductDao.findById(productId)
            ?: throw NotFoundException(MESSAGE_PRODUCT_NOT_FOUND)
        jdbcCartDao.addItemToCart(memberId, productId, quantity)
        return ResponseEntity.ok(MESSAGE_ADD_SUCCESS)
    }

    fun updateQuantity(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): ResponseEntity<String> {
        jdbcProductDao.findById(productId) ?: throw NotFoundException(MESSAGE_PRODUCT_NOT_FOUND)
        val affectedRows = jdbcCartDao.updateItemQuantityInCart(memberId, productId, quantity)
        when (affectedRows) {
            1 -> return ResponseEntity.ok().body(MESSAGE_UPDATE_SUCCESS)
            0 -> throw NotFoundException(MESSAGE_PRODUCT_NOT_FOUND_IN_CART)
            else -> throw InternalServerErrorException(MESSAGE_UNEXPECTED_ACTION_IN_CART)
        }
    }

    fun removeFromCart(
        memberId: Long,
        productId: Long,
    ): ResponseEntity<String> {
        jdbcProductDao.findById(productId) ?: throw NotFoundException(MESSAGE_PRODUCT_NOT_FOUND)
        val affectedRows = jdbcCartDao.removeItemFromCart(memberId, productId)
        when (affectedRows) {
            1 -> return ResponseEntity.ok().body(MESSAGE_REMOVE_SUCCESS)
            0 -> throw NotFoundException(MESSAGE_PRODUCT_NOT_FOUND_IN_CART)
            else -> throw InternalServerErrorException(MESSAGE_UNEXPECTED_ACTION_IN_CART)
        }
    }

    companion object {
        const val MESSAGE_PRODUCT_NOT_FOUND = "Product not found"
        const val MESSAGE_PRODUCT_NOT_FOUND_IN_CART = "Product not found in Cart"
        const val MESSAGE_UNEXPECTED_ACTION_IN_CART = "Unexpected action in Cart"
        const val MESSAGE_ADD_SUCCESS = "Item added to cart"
        const val MESSAGE_REMOVE_SUCCESS = "Item removed from cart"
        const val MESSAGE_UPDATE_SUCCESS = "Item updated in cart"
    }
}
