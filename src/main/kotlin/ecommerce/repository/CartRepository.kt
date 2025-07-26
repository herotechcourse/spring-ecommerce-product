package ecommerce.repository

import ecommerce.dto.CartItemDto
import ecommerce.exception.InternalServerErrorException
import ecommerce.exception.NotFoundException
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class CartRepository(private val jdbcClient: JdbcClient) {
    fun addItemToCart(
        productId: Long,
        productQuantity: Long,
        cartId: Long,
    ): CartItemDto {
        val sql =
            """
            INSERT INTO cart_items (product_id, cart_id, quantity)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE quantity = quantity + ?
            """.trimIndent()
        val rowsAffected =
            jdbcClient
                .sql(sql)
                .params(productId, cartId, productQuantity, productQuantity)
                .update()

        if (rowsAffected <= 0) throw NotFoundException("Failed to add: Product $productId not found")

        return showItem(cartId, productId) ?: throw NotFoundException("Failed to add: Product $productId not found")
    }

    fun removeItemFromCart(
        productId: Long,
        quantity: Long,
        cartId: Long,
    ): CartItemDto? {
        val currentQuantity = quantityInCart(productId, cartId)

        if (currentQuantity == 0L) {
            throw NotFoundException("Failed to delete: Product $productId not found in cart $cartId")
        } else if (currentQuantity > quantity) {
            decreaseItemQuantity(productId, quantity, cartId)
            return showItem(productId, cartId) ?: throw NotFoundException("Failed to delete: Product $productId not found in cart $cartId")
        } else {
            deleteItemRow(productId, cartId)
            return null
        }
    }

    private fun decreaseItemQuantity(
        productId: Long,
        quantity: Long,
        cartId: Long,
    ) {
        val sql =
            """
            UPDATE cart_items
            SET quantity = quantity - ?
            WHERE product_id = ? AND cart_id = ?
            """.trimIndent()

        val rowsAffected =
            jdbcClient
                .sql(sql)
                .params(quantity, productId, cartId)
                .update()

        if (rowsAffected == 0) {
            throw NotFoundException("Failed to delete: product $productId not found in cart $cartId")
        }
    }

    private fun deleteItemRow(
        productId: Long,
        cartId: Long,
    ) {
        val sql =
            """
            DELETE FROM cart_items
            WHERE product_id = ? AND cart_id = ?
            """.trimIndent()

        val rowsAffected =
            jdbcClient
                .sql(sql)
                .param(1, productId)
                .param(2, cartId)
                .update()

        if (rowsAffected == 0) {
            throw NotFoundException("Failed to delete: product $productId not found in cart $cartId")
        }
    }

    fun quantityInCart(
        productId: Long,
        cartId: Long,
    ): Long {
        val sql = "SELECT quantity FROM cart_items WHERE product_id = ? AND cart_id = ?"
        val quantity =
            jdbcClient
                .sql(sql)
                .params(productId, cartId)
                .query(Long::class.java)
                .optional()
                .orElse(0L)
        return quantity
    }

    fun findOrCreateCartId(userId: Long): Long {
        var cartId: Long?

        var sql = "SELECT cart_id FROM carts WHERE user_id = ?"
        cartId =
            jdbcClient
                .sql(sql)
                .query(Long::class.java)
                .optional()
                .orElse(null)

        if (cartId != null) return cartId

        sql = "INSERT INTO carts (user_id) VALUES (?)"
        cartId =
            jdbcClient
                .sql(sql)
                .param(1, userId)
                .query(Long::class.java)
                .optional()
                .orElse(null)

        return cartId ?: throw InternalServerErrorException("Cart could not be created")
    }

    fun showAllItemsInCart(cartId: Long): List<CartItemDto> {
        val sql =
            """
            SELECT
                ci.quantity,
                p.id AS product_id,
                p.name AS product_name,
                p.price AS product_price,
                p.image_url AS product_image_url
            FROM cart_items ci
            INNER JOIN products p ON ci.product_id = p.id
            WHERE ci.cart_id = ?
            """.trimIndent()
        val cartItems =
            jdbcClient
                .sql(sql)
                .param(1, cartId)
                .query(CartItemDto::class.java)
                .list()
        return cartItems
    }

    fun showItem(
        cartId: Long,
        productId: Long,
    ): CartItemDto? {
        val joinedSql =
            """
            SELECT 
                ci.quantity,
                ci.product_id,
                p.name AS product_name,
                p.price AS product_price,
                p.image_url AS product_image_url
            FROM cart_items ci
            JOIN products p ON ci.product_id = p.id
            WHERE ci.cart_id = ? AND ci.product_id = ?
            """.trimIndent()

        return jdbcClient
            .sql(joinedSql)
            .params(cartId, productId)
            .query(CartItemDto::class.java)
            .optional()
            .orElse(null)
    }
}
