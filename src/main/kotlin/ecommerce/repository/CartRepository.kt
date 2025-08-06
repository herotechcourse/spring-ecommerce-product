package ecommerce.repository

import ecommerce.dto.MemberResponse
import ecommerce.dto.TopProductStatResponse
import ecommerce.model.CartItem
import ecommerce.model.Role
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDateTime

@Repository
class CartRepository(val jdbcTemplate: JdbcTemplate) {
    private val rowMapper =
        RowMapper<CartItem> { rs: ResultSet, _ ->
            CartItem(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
                rs.getTimestamp("created_at"),
                updatedAt = rs.getTimestamp("updated_at"),
            )
        }

    fun insert(cartItem: CartItem): CartItem {
        val keyHolder = GeneratedKeyHolder()
        val sql =
            """
            INSERT INTO cart (member_id, product_id, quantity, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?)
            """.trimIndent()

        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, arrayOf("id"))
            ps.setLong(1, cartItem.memberId)
            ps.setLong(2, cartItem.productId)
            ps.setInt(3, cartItem.quantity)
            ps.setTimestamp(4, cartItem.createdAt)
            ps.setTimestamp(5, cartItem.updatedAt)
            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong() ?: throw IllegalStateException("Failed to retrieve ID")

        return cartItem.copy(id = generatedId)
    }

    fun updateQuantity(
        cartItem: CartItem,
        newQuantity: Int,
    ): CartItem {
        val sql =
            """
            UPDATE cart SET quantity = ?, updated_at = ? WHERE id = ?
            """.trimIndent()

        jdbcTemplate.update(sql, newQuantity, cartItem.updatedAt, cartItem.id)
        return cartItem.copy(quantity = newQuantity)
    }

    fun findByMemberId(memberId: Long): List<CartItem> {
        val sql = "SELECT * FROM cart WHERE member_id = ?"
        return jdbcTemplate.query(sql, rowMapper, memberId)
    }

    fun findById(id: Long): CartItem? {
        val sql = "SELECT * FROM cart WHERE id = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun findByMemberAndProductIds(
        memberId: Long,
        productId: Long,
    ): List<CartItem> {
        val sql = "SELECT * FROM cart WHERE member_id = ? AND product_id = ?"
        return jdbcTemplate.query(sql, rowMapper, memberId, productId)
    }

    fun deleteByMemberAndProduct(
        memberId: Long,
        productId: Long,
    ): Int? {
        val sql = "DELETE FROM cart WHERE member_id = ? AND product_id = ?"

        return try {
            jdbcTemplate.update(sql, memberId, productId).takeIf { it == 1 }
        } catch (ex: DataAccessException) {
            throw RuntimeException(
                "Failed to delete cart item belonging to member_id $memberId and product_id = $productId",
                ex,
            )
        }
    }

    fun findTopAddedProducts(
        count: Int,
        days: Int,
    ): List<TopProductStatResponse> {
        val updatedAt = Timestamp.valueOf(LocalDateTime.now().minusDays(days.toLong()))
        val sql =
            """
            SELECT 
                p.name,
                COUNT(*) AS times_added,
                MAX(c.updated_at) AS most_recent_added_time
            FROM cart c
            JOIN products p ON c.product_id = p.id
            WHERE c.updated_at >= ?
            GROUP BY c.product_id, p.name
            ORDER BY times_added DESC, most_recent_added_time DESC
            LIMIT ?
            """.trimIndent()

        return jdbcTemplate.query(sql, { rs, _ ->
            TopProductStatResponse(
                name = rs.getString("name"),
                count = rs.getInt("times_added"),
                lastAddedAt = rs.getTimestamp("most_recent_added_time"),
            )
        }, updatedAt, count)
    }

    fun getRecentActiveMembers(days: Int): List<MemberResponse> {
        val updatedAt = Timestamp.valueOf(LocalDateTime.now().minusDays(days.toLong()))
        val sql =
            """
            SELECT DISTINCT m.id, m.name, m.email, m.role
            FROM members m
            JOIN cart c ON m.id = c.member_id
            WHERE c.created_at >= ?
            """.trimIndent()

        return jdbcTemplate.query(sql, { rs, _ ->
            MemberResponse(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                email = rs.getString("email"),
                role = Role.valueOf(rs.getString("role")),
            )
        }, updatedAt)
    }
}
