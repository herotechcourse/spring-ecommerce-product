package ecommerce.repository

import ecommerce.dto.ActiveUsersResponse
import ecommerce.dto.TopProductStats
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
class AdminStatsRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    private val cartItemForMostWanted =
        RowMapper<TopProductStats> { rs, _ ->
            TopProductStats(
                rs.getString("product_name"),
                rs.getInt("added_count"),
                rs.getTimestamp("last_added_at"),
            )
        }

    private val activeUsers =
        RowMapper<ActiveUsersResponse> { rs, _ ->
            ActiveUsersResponse(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
            )
        }

    fun get5MostAddedProducts(): List<TopProductStats> {
        val sql =
            """
            SELECT 
                p.name AS product_name,
                COUNT(*) AS added_count,
                MAX(ci.created_at) AS last_added_at
            FROM cart_history ci
            JOIN products p ON ci.product_id = p.id
            WHERE ci.created_at >= CURRENT_DATE - INTERVAL '30' DAY
            GROUP BY ci.product_id, p.name
            ORDER BY added_count DESC, last_added_at DESC
            LIMIT 5
            """.trimIndent()

        val products = jdbcTemplate.query(sql, cartItemForMostWanted)
        return products
    }

    fun addProductToStats(
        productId: Long,
        cartId: Long,
    ): Long {
        val now = Timestamp(System.currentTimeMillis())
        val insert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("CART_HISTORY")
                .usingGeneratedKeyColumns("id")

        val productMap =
            mapOf(
                "cart_id" to cartId,
                "product_id" to productId,
                "status" to "ADDED",
                "created_at" to now,
            )
        return insert.executeAndReturnKey(productMap).toLong()
    }

    fun getTop5ActiveUsers(): List<ActiveUsersResponse> {
        val sql =
            """
            SELECT DISTINCT m.ID, m.NAME, m.EMAIL FROM MEMBERS m
            JOIN CARTS c ON m.id = c.MEMBER_ID
            RIGHT JOIN CART_HISTORY ch ON ch.CART_ID = c.ID
            WHERE ch.CREATED_AT >= CURRENT_DATE - INTERVAL '7' DAY
            LIMIT 5
            """.trimIndent()
        return jdbcTemplate.query(sql, activeUsers)
    }
}
