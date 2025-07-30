package ecommerce.repository

import ecommerce.dto.stats.TopProductStats
import ecommerce.model.CartItem
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.UUID

@Repository
class CartItemRepository(private val jdbcTemplate: JdbcTemplate) {
    private val carItemRowMapper: (ResultSet, Int) -> CartItem = { rs, _ ->
        CartItem(
            id = rs.getLong("id"),
            memberId = UUID.fromString(rs.getString("member_id")),
            productId = rs.getLong("product_id"),
            quantity = rs.getInt("quantity"),
            createdAt = rs.getObject("created_at", LocalDateTime::class.java),
        )
    }

    fun save(cartItem: CartItem): CartItem {
        return if (cartItem.id == 0L) {
            val sql = "INSERT INTO cart_items (member_id, product_id, quantity, created_at) VALUES(?,?,?,?)"
            val keyHolder: KeyHolder = GeneratedKeyHolder()
            jdbcTemplate.update({ connection ->
                val ps = connection.prepareStatement(sql, arrayOf("id"))
                ps.setString(1, cartItem.memberId.toString())
                ps.setLong(2, cartItem.productId)
                ps.setInt(3, cartItem.quantity)
                ps.setObject(4, cartItem.createdAt)
                ps
            }, keyHolder)
            val generatedId = keyHolder.key?.toLong() ?: 0L
            cartItem.copy(id = generatedId)
        } else {
            val sql = "UPDATE cart_items SET quantity = ?, created_at = ? WHERE id = ?"
            jdbcTemplate.update(sql, cartItem.quantity, cartItem.createdAt, cartItem.id)
            cartItem
        }
    }

    fun findByMemberIdAndProductId(
        memberId: UUID,
        productId: Long,
    ): CartItem? {
        val sql =
            "SELECT id, member_id, product_id, quantity, created_at FROM cart_items WHERE member_id = ? AND product_id = ?"
        return try {
            jdbcTemplate.query(sql, carItemRowMapper, memberId.toString(), productId).firstOrNull()
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun findAllByMemberId(memberId: UUID): List<CartItem> {
        val sql = "SELECT * from cart_items WHERE member_id = ?"
        return jdbcTemplate.query(sql, carItemRowMapper, memberId.toString())
    }

    fun deleteById(id: Long) {
        val sql = "DELETE FROM cart_items WHERE id = ?"
        jdbcTemplate.update(sql, id)
    }

    fun findTop5AddedProducts(since: LocalDateTime): List<TopProductStats> {
        val sql =
            """
            SELECT
                p.id AS product_id,
                p.name AS product_name,
                COUNT(ci.product_id) AS times_added,
                MAX(ci.created_at) AS most_recent_added_time
            FROM
                cart_items ci
            JOIN
                products p ON ci.product_id = p.id
            WHERE
                ci.created_at >= ?
            GROUP BY
                p.id, p.name
            ORDER BY
                times_added DESC, most_recent_added_time DESC
            LIMIT 5
            """.trimIndent()
        return jdbcTemplate.query(sql, { rs, _ ->
            TopProductStats(
                productId = rs.getLong("product_id"),
                productName = rs.getString("product_name"),
                timesAdded = rs.getInt("times_added"),
                mostRecentAddedTime = rs.getObject("most_recent_added_time", LocalDateTime::class.java),
            )
        }, since)
    }

    fun findRecentlyActiveMemberIds(since: LocalDateTime): List<UUID> {
        val sql = """
            SELECT DISTINCT ci.member_id
            FROM cart_items ci
            WHERE ci.created_at >= ?        
        """
        return jdbcTemplate.query(sql, { rs, _ ->
            UUID.fromString(rs.getString("member_id"))
        }, since)
    }
}
