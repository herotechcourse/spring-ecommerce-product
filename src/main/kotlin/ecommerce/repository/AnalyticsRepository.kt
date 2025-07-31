package ecommerce.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AnalyticsRepository(private val jdbc: JdbcTemplate) {
    fun findTop5MostAddedProductsLast30Days(): List<Map<String, Any?>> {
        val sql =
            """
            SELECT 
                p.name as productName,
                SUM(c.quantity) as addedCount,
                MAX(c.added_at) as mostRecentAdded
            FROM cart c
            JOIN products p ON c.product_id = p.id
            WHERE c.added_at >= DATEADD('DAY', -30, CURRENT_TIMESTAMP)
            GROUP BY c.product_id, p.name
            ORDER BY SUM(c.quantity) DESC, MAX(c.added_at) DESC
            LIMIT 5
            """.trimIndent()

        return try {
            jdbc.query(sql) { rs, _ ->
                mapOf(
                    "productName" to rs.getString("productName"),
                    "addedCount" to rs.getInt("addedCount"),
                    "mostRecentAdded" to rs.getTimestamp("mostRecentAdded")?.toLocalDateTime(),
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun findMembersActiveInLast7Days(): List<Map<String, Any?>> {
        val sql =
            """
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

        return try {
            jdbc.query(sql) { rs, _ ->
                mapOf(
                    "memberId" to rs.getLong("memberId"),
                    "memberName" to rs.getString("memberName"),
                    "memberEmail" to rs.getString("memberEmail"),
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
