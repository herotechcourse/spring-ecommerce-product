package ecommerce.dao

import ecommerce.model.CartItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcCartDAO(private val db: JdbcTemplate) : CartDAO {
    private val cartItemRowMapper =
        RowMapper<CartItem> { rs: ResultSet, _ ->
            CartItem(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
            )
        }

    override fun addItemToCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Long {
        val existing = isItemExistInCart(memberId, productId)
        return when (existing) {
            true -> addItemIfExistInCart(memberId, productId, quantity)
            false -> addItemIfNotExistInCart(memberId, productId, quantity)
        }
    }

    private fun addItemIfExistInCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Long {
        val keyHolder = GeneratedKeyHolder()
        db.update(
            { connection ->
                connection.prepareStatement(
                    "UPDATE cart_item SET quantity = quantity + ? WHERE member_id = ? AND product_id = ?",
                    arrayOf("id"),
                ).apply {
                    setInt(1, quantity)
                    setLong(2, memberId)
                    setLong(3, productId)
                }
            },
            keyHolder,
        )
        return keyHolder.key?.toLong() ?: throw IllegalStateException("insert - Failed to retrieve ID")
    }

    private fun addItemIfNotExistInCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Long {
        val keyHolder = GeneratedKeyHolder()
        db.update(
            { connection ->
                connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    arrayOf("id"),
                ).apply {
                    setLong(1, memberId)
                    setLong(2, productId)
                    setInt(3, quantity)
                }
            },
            keyHolder,
        )
        return keyHolder.key?.toLong() ?: throw IllegalStateException("insert - Failed to retrieve ID")
    }

    override fun getCartItemsByMemberId(memberId: Long): List<CartItem> {
        val sql = "SELECT * FROM cart_item WHERE member_id = ?"
        return db.query(sql, cartItemRowMapper, memberId)
    }

    override fun removeItemFromCart(
        memberId: Long,
        productId: Long,
    ): Int {
        val sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?"
        return db.update(sql, memberId, productId)
    }

    override fun updateItemQuantityInCart(
        memberId: Long,
        productId: Long,
        quantity: Int,
    ): Int {
        val sql = "UPDATE cart_item SET quantity = ? WHERE member_id = ? AND product_id = ?"
        return db.update(sql, quantity, memberId, productId)
    }

    private fun isItemExistInCart(
        memberId: Long,
        productId: Long,
    ): Boolean {
        val sql = "SELECT COUNT(*) FROM cart_item WHERE member_id = ? AND product_id = ?"
        val count = db.queryForObject(sql, Long::class.java, memberId, productId)
        return count != null && count > 0
    }
}
