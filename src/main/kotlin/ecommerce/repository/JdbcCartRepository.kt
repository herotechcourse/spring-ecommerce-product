package ecommerce.repository

import ecommerce.model.CartItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime

@Repository
class JdbcCartRepository(
    private val jdbcTemplate: JdbcTemplate,
) : CartRepository {
    private val rowMapper =
        RowMapper { rs, _ ->
            CartItem(
                memberId = rs.getLong("member_id"),
                productId = rs.getLong("product_id"),
                quantity = rs.getInt("quantity"),
                addedAt = rs.getTimestamp("added_at").toLocalDateTime(),
            )
        }

    override fun findByMemberId(memberId: Long?): List<CartItem> {
        val sql = "SELECT * FROM cart_items WHERE member_id = ?"
        return jdbcTemplate.query(sql, rowMapper, memberId)
    }

    override fun findByMemberIdAndProductId(
        memberId: Long?,
        productId: Long,
    ): CartItem? {
        val sql = "SELECT * FROM cart_items WHERE member_id = ? AND product_id = ?"
        return jdbcTemplate.query(sql, rowMapper, memberId, productId)
            .firstOrNull()
    }

    override fun insert(
        memberId: Long?,
        productId: Long,
        quantity: Int,
    ) {
        val sql =
            """
            MERGE INTO cart_items (member_id, product_id, quantity, added_at)
            KEY (member_id, product_id)
            VALUES (?, ?, ?, ?)
            """.trimIndent()
        val now = LocalDateTime.now()
        jdbcTemplate.update(sql, memberId, productId, quantity, Timestamp.valueOf(now))
    }

    override fun updateQuantity(
        memberId: Long?,
        productId: Long,
        quantity: Int,
    ) {
        val sql = "UPDATE cart_items SET quantity = ? WHERE member_id = ? AND product_id = ?"
        jdbcTemplate.update(sql, quantity, memberId, productId)
    }

    override fun delete(
        memberId: Long?,
        productId: Long,
    ) {
        val sql = "DELETE FROM cart_items WHERE member_id = ? AND product_id = ?"
        jdbcTemplate.update(sql, memberId, productId)
    }
}
