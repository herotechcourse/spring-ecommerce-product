package ecommerce.repository

import ecommerce.model.CartItem
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement
import java.util.UUID

@Repository
class CartItemRepository(private val jdbcTemplate: JdbcTemplate) {
    private val carItemRowMapper: (ResultSet, Int) -> CartItem = { rs, _ ->
        CartItem(
            id = rs.getLong("id"),
            memberId = UUID.fromString(rs.getString("member_id")),
            productId = rs.getLong("product_id"),
            quantity = rs.getInt("quantity"),
        )
    }

    fun save(cartItem: CartItem): CartItem {
        return if (cartItem.id == 0L) {
            val sql = "INSERT INTO cart_items (member_id, product_id, quantity) VALUES(?,?,?)"
            val keyHolder: KeyHolder = GeneratedKeyHolder()
            jdbcTemplate.update({ connection ->
                val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                ps.setString(1, cartItem.memberId.toString())
                ps.setLong(2, cartItem.productId)
                ps.setInt(3, cartItem.quantity)
                ps
            }, keyHolder)
            val generatedId = keyHolder.key?.toLong() ?: 0L
            cartItem.copy(id = generatedId)
        } else {
            val sql = "UPDATE cart_items SET quantity = ? WHERE id = ?"
            jdbcTemplate.update(sql, cartItem.quantity, cartItem.id)
            cartItem
        }
    }

    fun findByMemberIdAndProductId(
        memberId: UUID,
        productId: Long,
    ): CartItem? {
        val sql = "SELECT id, member_id, product_id, quantity FROM cart_items WHERE member_id = ? AND product_id = ?"
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

    fun findByProductId(productId: Long): CartItem? {
        val sql = "SELECT * from cart_items WHERE product_id = ?"
        return jdbcTemplate.query(sql, carItemRowMapper, productId).firstOrNull()
    }

    fun deleteById(id: Long) {
        val sql = "DELETE FROM cart_items WHERE id = ?"
        jdbcTemplate.update(sql, id)
    }

    fun deleteByMemberIdAndProductId(
        memberId: UUID,
        productId: Long,
    ) {
        val sql = "DELETE FROM cart_items WHERE member_id = ? AND product_id = ?"
        jdbcTemplate.update(sql, memberId.toString(), productId)
    }
}
