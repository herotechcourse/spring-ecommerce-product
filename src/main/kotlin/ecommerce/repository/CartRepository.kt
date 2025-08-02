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
                ex
            )
        }
    }
}
