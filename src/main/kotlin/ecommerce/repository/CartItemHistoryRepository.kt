package ecommerce.repository

import ecommerce.entity.CartItemHistory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CartItemHistoryRepository(private val jdbc: JdbcTemplate) {

    fun insert(history: CartItemHistory) {
        jdbc.update(
            """
            INSERT INTO cart_item_history (user_id, product_id, quantity, action)
            VALUES (?, ?, ?, ?)
            """.trimIndent(),
            history.userId, history.productId, history.quantity, history.action
        )
    }

    fun findTop5MostAddedProducts(): List<Map<String, Any>> {
        val sql = """
        SELECT p.name AS product_name,
               COUNT(*) AS add_count,
               MAX(h.created_at) AS latest_added_at
        FROM cart_item_history h
        JOIN products p ON h.product_id = p.id
        WHERE h.action = 'ADD'
          AND h.created_at >= DATEADD('DAY', -30, CURRENT_TIMESTAMP)
        GROUP BY p.id, p.name
        ORDER BY add_count DESC, latest_added_at DESC
        LIMIT 5
    """.trimIndent()

        return jdbc.queryForList(sql)
    }

}
