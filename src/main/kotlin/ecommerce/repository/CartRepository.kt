package ecommerce.repository

import ecommerce.entity.CartItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement

@Repository
class CartRepository(private val jdbcTemplate: JdbcTemplate) {
    private val rowMapper =
        RowMapper<CartItem> { rs, _ ->
            CartItem(
                id = rs.getLong("id"),
                userId = rs.getLong("user_id"),
                productId = rs.getLong("product_id"),
                quantity = rs.getInt("quantity"),
            )
        }

    fun findByUserId(userId: Long): List<CartItem> {
        val sql = "SELECT * FROM cart_items WHERE user_id = ?"
        return jdbcTemplate.query(sql, rowMapper, userId)
    }

    fun findByUserIdAndProductId(
        userId: Long,
        productId: Long,
    ): CartItem? {
        val sql = "SELECT * FROM cart_items WHERE user_id = ? AND product_id = ?"
        return jdbcTemplate.query(sql, rowMapper, userId, productId).firstOrNull()
    }

    fun create(cartItem: CartItem): Long {
        val sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)"
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, arrayOf("id"))
            ps.setLong(1, cartItem.userId)
            ps.setLong(2, cartItem.productId)
            ps.setInt(3, cartItem.quantity)
            ps
        }, keyHolder)
        val id = keyHolder.key!!.toLong()
        return id
    }

    fun updateQuantity(
        userId: Long,
        productId: Long,
        quantity: Int,
    ): Boolean {
        val sql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?"
        val rowsAffected = jdbcTemplate.update(sql, quantity, userId, productId)
        return rowsAffected > 0
    }

    fun deleteByUserIdAndProductId(
        userId: Long,
        productId: Long,
    ): Boolean {
        val sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?"
        val rowsAffected = jdbcTemplate.update(sql, userId, productId)
        return rowsAffected > 0
    }
}
