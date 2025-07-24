package ecommerce.repository

import ecommerce.dto.CartItemResponse
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class CartItemRepository(private val jdbcTemplate: JdbcTemplate) {
    private val cartItemRowMapper =
        RowMapper<CartItemResponse> { rs, _ ->
            CartItemResponse(
                rs.getLong("product_id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getDouble("price"),
                rs.getString("image_url"),
            )
        }

    fun addProductToCart(
        productId: Long,
        cartId: Long,
        quantity: Int,
    ): Long {
        val insert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_items")
                .usingGeneratedKeyColumns("id")

        val productMap =
            mapOf(
                "cart_id" to cartId,
                "product_id" to productId,
                "quantity" to quantity,
            )
        return insert.executeAndReturnKey(productMap).toLong()
    }

    fun deleteProductFromCart(cartItemId: Long): Boolean {
        return jdbcTemplate.update("DELETE FROM cart_items WHERE id =?", cartItemId) > 0
    }

    fun getCartItemsByCartId(cartId: Long): List<CartItemResponse> {
        val sql =
            """
            SELECT
            p.id as product_id,
            p.name,
            p.price,
            p.image_url,
            cart.quantity
            FROM cart_items cart
            JOIN products p ON cart.product_id = p.id
            WHERE cart.cart_id = ?
            """.trimIndent()

        return jdbcTemplate.query(sql, cartItemRowMapper, cartId)
    }

    fun deleteCartItemsByCartIdAndProductId(
        cartId: Long,
        productId: Long,
    ): Boolean {
        return jdbcTemplate.update("DELETE FROM cart_items WHERE cart_id =? AND product_id =?", cartId, productId) > 0
    }

    fun updateQuantityByCartIdAndProductId(
        cartId: Long,
        productId: Long,
        newQuantity: Int,
    ): Boolean {
        return jdbcTemplate.update(
            """
            UPDATE cart_items SET quantity =? WHERE cart_id =? AND product_id =?
            """.trimIndent(),
            newQuantity,
            cartId,
            productId,
        ) > 0
    }
}
