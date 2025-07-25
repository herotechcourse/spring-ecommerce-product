package ecommerce.repository

import ecommerce.model.CartItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

@Repository
class CartRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    private val rowMapper = RowMapper<CartItem> { rs: ResultSet, _ ->
        CartItem(
            id = rs.getLong("id"),
            memberId = rs.getLong("member_id"),
            productId = rs.getLong("product_id"),
            createdAt = rs.getTimestamp("created_at").toLocalDateTime()
        )
    }

    fun addToCart(memberId: Long, productId: Long) {
        jdbcTemplate.update(
            "INSERT INTO cart_items (member_id, product_id) VALUES (?, ?)",
            memberId,
            productId
        )
    }

    fun getCartItems(memberId: Long): List<CartItem> {
        return jdbcTemplate.query(
            "SELECT * FROM cart_items WHERE member_id = ?",
            rowMapper,
            memberId
        )
    }

    fun removeFromCart(memberId: Long, productId: Long) {
        jdbcTemplate.update(
            "DELETE FROM cart_items WHERE member_id = ? AND product_id = ?",
            memberId,
            productId
        )
    }
}
