package ecommerce.service

import ecommerce.dao.JdbcCartDao
import ecommerce.dao.JdbcProductDao
import ecommerce.exception.NotFoundException
import ecommerce.model.CartItem
import org.springframework.stereotype.Service

@Service
class CartService(
    private val jdbcCartDAO: JdbcCartDao,
    private val jdbcProductDAO: JdbcProductDao,
) {
    fun getCart(memberId: Long): List<CartItem> {
        return jdbcCartDAO.getCartItemsByMemberId(memberId)
    }

    fun addToCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Long {
        jdbcProductDAO.findById(productId)
            ?: throw NotFoundException("Product with id: $productId not found")
        return jdbcCartDAO.addItemToCart(memberId, productId, quantity)
    }

    fun removeFromCart(
        memberId: Long,
        productId: Long,
    ): Int {
        return jdbcCartDAO.removeItemFromCart(memberId, productId)
    }

    fun updateQuantity(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Int {
        return jdbcCartDAO.updateItemQuantityInCart(memberId, productId, quantity)
    }
}
