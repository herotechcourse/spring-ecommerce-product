package ecommerce.repository

import ecommerce.domain.CartItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement

@Repository
class CartItemRepository(private val jdbcTemplate: JdbcTemplate) {
    private val cartItemMapper =
        RowMapper<CartItem> { rs: ResultSet, _ ->
            CartItem(
                rs.getLong("id"),
                rs.getLong("cart_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
            )
        }

    fun findByCartId(cartId: Long): List<CartItem> {
        val sql = "SELECT * FROM cart_items WHERE cart_id = ?"
        return jdbcTemplate.query(sql, cartItemMapper, cartId)
    }

    fun findByCartIdAndProductId(
        cartId: Long,
        productId: Long,
    ): CartItem? {
        val sql = "SELECT * FROM cart_items WHERE cart_id = ? AND product_id = ?"
        return jdbcTemplate.query(sql, cartItemMapper, cartId, productId).firstOrNull()
    }

    fun create(cartItem: CartItem) {
        val sql = "insert into cart_items (cart_id, product_id, quantity) values (?,?,?)"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setLong(1, cartItem.cartId)
            ps.setLong(2, cartItem.productId)
            ps.setInt(3, cartItem.quantity)
            ps
        }, keyHolder)

        cartItem.id = keyHolder.key?.toLong() ?: throw IllegalStateException("Failed to retrieve generated ID for cart_item.")
    }

    fun update(cartItem: CartItem) {
        val sql = "update cart_items set quantity = ? where id = ?"
        jdbcTemplate.update(sql, cartItem.quantity, cartItem.id)
    }

    fun deleteByCartIdAndProductId(
        cartId: Long,
        productId: Long,
    ) {
        val sql = "delete from cart_items where cart_id = ? and product_id = ?"
        jdbcTemplate.update(sql, cartId, productId)
    }
}
