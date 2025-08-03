package ecommerce.repository

import ecommerce.dto.ActiveMemberStat
import ecommerce.dto.TopProductStat
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcCartStatRepository(
    private val jdbcTemplate: JdbcTemplate,
) : CartStatRepository {
    override fun findTop5MostAddedProductsInLast30Days(): List<TopProductStat> {
        val sql =
            """
            SELECT 
                ci.product_id,
                p.name AS product_name,
                COUNT(*) AS added_count,
                MAX(ci.added_at) AS last_added_at
            FROM cart_items ci
            JOIN products p ON ci.product_id = p.id
            WHERE ci.added_at >= DATEADD('DAY', -30, CURRENT_TIMESTAMP)
            GROUP BY ci.product_id, p.name
            ORDER BY added_count DESC, last_added_at DESC
            LIMIT 5
            """.trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            TopProductStat(
                productId = rs.getLong("product_id"),
                productName = rs.getString("product_name"),
                addedCount = rs.getInt("added_count"),
                lastAddedAt = rs.getTimestamp("last_added_at").toLocalDateTime(),
            )
        }
    }

    override fun findActiveMembersInLast7Days(): List<ActiveMemberStat> {
        val sql =
            """
            SELECT DISTINCT 
                m.id AS member_id,
                m.name,
                m.email
            FROM members m
            JOIN cart_items ci ON m.id = ci.member_id
            WHERE ci.added_at >= DATEADD('DAY', -7, CURRENT_TIMESTAMP)
            """.trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            ActiveMemberStat(
                memberId = rs.getLong("member_id"),
                name = rs.getString("name"),
                email = rs.getString("email"),
            )
        }
    }
}
