package ecommerce.repository

import ecommerce.model.Cart
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime

@Repository
class CartRepository(private val jdbc: JdbcTemplate) {
    private val cartRowMapper =
        RowMapper<Cart> { rs, _ ->
            Cart(
                id = rs.getLong("id"),
                memberId = rs.getLong("member_id"),
                productId = rs.getLong("product_id"),
                quantity = rs.getInt("quantity"),
                addedAt = rs.getTimestamp("added_at")?.toLocalDateTime(),
            )
        }

    fun save(cart: Cart): Cart {
        val sql =
            """
            INSERT INTO cart (member_id, product_id, quantity, added_at)
            VALUES (?, ?, ?, ?)
            """.trimIndent()

        val addedAt = cart.addedAt ?: LocalDateTime.now()

        jdbc.update(
            sql,
            cart.memberId,
            cart.productId,
            cart.quantity,
            Timestamp.valueOf(addedAt),
        )
        return cart.copy(addedAt = addedAt)
    }

    fun findByUserId(userId: Long): List<Cart> {
        val sql = "SELECT * FROM cart WHERE member_id = ?"
        return jdbc.query(sql, cartRowMapper, userId)
    }

    fun findByUserIdAndProductId(
        userId: Long,
        productId: Long,
    ): Cart? {
        val sql = "SELECT * FROM cart WHERE member_id = ? AND product_id = ?"
        val results = jdbc.query(sql, cartRowMapper, userId, productId)
        return results.firstOrNull()
    }

    fun update(cart: Cart): Cart {
        val sql =
            """
            UPDATE cart 
            SET quantity = ?, added_at = ?
            WHERE member_id = ? AND product_id = ?
            """.trimIndent()

        val addedAt = cart.addedAt ?: LocalDateTime.now()

        jdbc.update(
            sql,
            cart.quantity,
            Timestamp.valueOf(addedAt),
            cart.memberId,
            cart.productId,
        )
        return cart.copy(addedAt = addedAt)
    }

    fun deleteByUserIdAndProductId(
        userId: Long,
        productId: Long,
    ) {
        val sql = "DELETE FROM cart WHERE member_id = ? AND product_id = ?"
        jdbc.update(sql, userId, productId)
    }

    fun deleteByUserId(userId: Long) {
        val sql = "DELETE FROM cart WHERE member_id = ?"
        jdbc.update(sql, userId)
    }

    fun findAll(): List<Cart> {
        val sql = "SELECT * FROM cart"
        return jdbc.query(sql, cartRowMapper)
    }

    fun findTop5MostAddedProductsLast30Days(): List<Map<String, Any>> {
        val sql = """
            SELECT 
                p.name as productName,
                COUNT(*) as addedCount,
                MAX(c.added_at) as mostRecentAdded
            FROM cart c
            JOIN products p ON c.product_id = p.id
            WHERE c.added_at >= DATEADD('DAY', -30, CURRENT_TIMESTAMP)
            GROUP BY c.product_id, p.name
            ORDER BY COUNT(*) DESC, MAX(c.added_at) DESC
            LIMIT 5
        """.trimIndent()

        return jdbc.query(sql) { rs, _ ->
            mapOf(
                "productName" to rs.getString("productName") as Any,
                "addedCount" to rs.getInt("addedCount") as Any,
                "mostRecentAdded" to (rs.getTimestamp("mostRecentAdded")?.toLocalDateTime() ?: "") as Any
            )
        }
    }

    fun findMembersActiveInLast7Days(): List<Map<String, Any>> {
        val sql = """
            SELECT DISTINCT 
                m.id as memberId,
                m.name as memberName,
                m.email as memberEmail
            FROM members m
            WHERE EXISTS (
                SELECT 1 FROM cart c 
                WHERE c.member_id = m.id 
                AND c.added_at >= DATEADD('DAY', -7, CURRENT_TIMESTAMP)
            )
        """.trimIndent()

        return jdbc.query(sql) { rs, _ ->
            mapOf(
                "memberId" to rs.getLong("memberId") as Any,
                "memberName" to rs.getString("memberName") as Any,
                "memberEmail" to rs.getString("memberEmail") as Any
            )
        }
    }
}
