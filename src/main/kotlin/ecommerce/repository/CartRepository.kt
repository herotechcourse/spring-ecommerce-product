package ecommerce.repository

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import ecommerce.dto.RawCartItemDto

@Repository
class CartRepository(private val jdbcClient: JdbcClient) {
    fun addProductToCart(productId: Long, cartId: Long): Boolean {
        val sql = """
        INSERT INTO cart_items (product_id, cart_id, quantity)
        VALUES (?, ?, 1)
        ON DUPLICATE KEY UPDATE quantity = quantity + 1
        """.trimIndent()
        val rowsAffected = jdbcClient
            .sql(sql)
            .params(productId, cartId)
            .update()

        return rowsAffected > 0
    }

    fun removeProductFromCart(productId: Long, cartId: Long): Boolean {
        val quantity = quantityInCart(productId, cartId)
        var sql: String

        if (quantity == 0L) {
            return false
        }
        else if (quantity > 1) {
            // decrease quantity
            sql = """
                UPDATE cart_items
                SET quantity = quantity - 1
                WHERE product_id = ? AND cart_id = ?
            """.trimIndent()
        } else {
            // delete whole row
            sql = """
                DELETE FROM cart_items
                WHERE product_id = ? AND cart_id = ?
            """.trimIndent()
        }

        val rowsAffected = jdbcClient
            .sql(sql)
            .param(1, productId)
            .param(2, cartId)
            .update()

        return rowsAffected > 0
    }

    fun quantityInCart(productId: Long, cartId: Long): Long {
        val sql = "SELECT quantity FROM cart_items WHERE product_id = ? AND cart_id = ?"
        val quantity = jdbcClient
            .sql(sql)
            .params(productId, cartId)
            .query(Long::class.java)
            .optional()
            .orElse(0L)
        return quantity
    }

    fun findOrCreateCartId(userId: Long): Long? {
        var cartId: Long?

        var sql: String = "SELECT cart_id FROM carts WHERE user_id = ?"
        cartId = jdbcClient
            .sql(sql)
            .query(Long::class.java)
            .optional()
            .orElse(null)
        if (cartId != null) return cartId

        sql = "INSERT INTO carts (user_id) VALUES (?)"
        cartId = jdbcClient
            .sql(sql)
            .param(1, userId)
            .query(Long::class.java)
            .single()
        return cartId
    }

    fun showAllItemsInCart(cartId: Long): List<RawCartItemDto> {
        val sql = """
            SELECT cart_id, product_id, quantity
            FROM cart_items
            WHERE cart_id = ?
        """.trimIndent()
        val cartItems = jdbcClient
            .sql(sql)
            .param(1, cartId)
            .query(RawCartItemDto::class.java)
            .list()
        return cartItems
    }

}
