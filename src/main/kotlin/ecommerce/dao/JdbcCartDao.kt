package ecommerce.dao

import ecommerce.dto.ActiveMemberInfo
import ecommerce.dto.TopProductStats
import ecommerce.model.CartItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcCartDao(private val db: JdbcTemplate) : CartDao {
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
        return keyHolder.key?.toLong() ?: throw IllegalStateException(MESSAGE_INSERT_RETRIEVE_ID_FAILED)
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
        db.update(
            "INSERT INTO cart_item_event (member_id, product_id) VALUES (?, ?)",
            memberId,
            productId,
        )
        return keyHolder.key?.toLong() ?: throw IllegalStateException(MESSAGE_INSERT_RETRIEVE_ID_FAILED)
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

    fun getTop5AddedProductsInLast30Days(): List<TopProductStats> {
        val sql =
            """
            SELECT p.name, COUNT(e.id) AS add_count, MAX(e.created_at) AS last_added_at
            FROM cart_item_event e
            JOIN product p ON e.product_id = p.id
            WHERE e.created_at >= NOW() - INTERVAL '30' DAY
            GROUP BY p.id, p.name
            ORDER BY add_count DESC, last_added_at DESC
            LIMIT 5
            """.trimIndent()

        return db.query(sql) { rs, _ ->
            TopProductStats(
                name = rs.getString("name"),
                addCount = rs.getInt("add_count"),
                lastAddedAt = rs.getTimestamp("last_added_at").toLocalDateTime(),
            )
        }
    }

    fun getActiveMembersInLast7Days(): List<ActiveMemberInfo> {
        val sql =
            """
            SELECT DISTINCT m.id, m.email
            FROM member m
            WHERE EXISTS (
                SELECT 1 FROM cart_item_event e
                WHERE e.member_id = m.id
                    AND e.created_at >= NOW() - INTERVAL '7' DAY
            )
            """.trimIndent()

        return db.query(sql) { rs, _ ->
            ActiveMemberInfo(
                id = rs.getLong("id"),
                email = rs.getString("email"),
            )
        }
    }

    companion object {
        const val MESSAGE_INSERT_RETRIEVE_ID_FAILED = "insert - Failed to retrieve ID"
    }
}
