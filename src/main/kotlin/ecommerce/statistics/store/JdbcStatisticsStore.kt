package ecommerce.statistics.store

import ecommerce.auth.model.MemberDTO
import ecommerce.statistics.model.ProductStatsDTO
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcStatisticsStore(private val db: JdbcTemplate) : StatisticsStore {
    override fun find5MostAddedProductsLast30Days(): List<ProductStatsDTO> {
        val sql =
            """
            SELECT p.name, COUNT(*) AS times_added, MAX(ci.added_at) AS most_recent_added_time
            FROM cart_item ci
            JOIN product p ON p.id = ci.product_id
            WHERE ci.added_at >= DATEADD('DAY', -30, CURRENT_DATE)
            GROUP BY p.id, p.name
            ORDER BY times_added DESC, most_recent_added_time DESC
            LIMIT 5
            """.trimIndent()

        return db.query(sql) { rs, _ ->
            ProductStatsDTO(
                name = rs.getString("name"),
                timesAdded = rs.getInt("times_added"),
                mostRecentAddedTime = rs.getTimestamp("most_recent_added_time").toLocalDateTime(),
            )
        }
    }

    override fun findMembersAddedToCartLast7Days(): List<MemberDTO> {
        val sql =
            """
            SELECT DISTINCT m.id, m.email, m.name
            FROM member m
            JOIN cart c ON c.member_id = m.id
            JOIN cart_item ci ON ci.cart_id = c.id
            WHERE ci.added_at >= DATEADD('DAY', -7, CURRENT_DATE)
            """.trimIndent()

        return db.query(sql) { rs, _ ->
            MemberDTO(
                id = rs.getLong("id"),
                email = rs.getString("email"),
                name = rs.getString("name"),
            )
        }
    }
}
