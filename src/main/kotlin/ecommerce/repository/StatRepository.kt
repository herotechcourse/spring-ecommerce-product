package ecommerce.repository

import ecommerce.dto.MemberStatsDto
import ecommerce.dto.ProductStatsDto
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class StatRepository(private val jdbcTemplate: JdbcTemplate) {
    fun getActiveMembersInThePast7Days(): List<MemberStatsDto> {
        val query = """
            SELECT DISTINCT
                m.id AS member_id,
                m.email
            FROM
                cart_items ci
            JOIN
                carts c ON ci.cart_id = c.cart_id
            JOIN
                members m ON c.user_id = m.id
            WHERE
                ci.created_at >= DATEADD('DAY', -7, CURRENT_DATE);
        """

        return jdbcTemplate.query(query) { rs, _ ->
            MemberStatsDto(
                memberId = rs.getLong("member_id"),
                email = rs.getString("email"),
                name = "",
            )
        }
    }

    fun getTop5ProductsInThePast30Days(): List<ProductStatsDto> {
        val query = """
            SELECT
                p.name AS product_name,
                COUNT(ci.quantity) AS product_quantity,
                MAX(ci.created_at) AS most_recent
            FROM
                cart_items ci
            JOIN
                products p ON ci.product_id = p.id
            WHERE
                ci.created_at >= DATEADD('DAY', -30, CURRENT_DATE)
            GROUP BY
                p.name
            ORDER BY
                product_quantity DESC,
                most_recent DESC
            LIMIT 5;
        """

        return jdbcTemplate.query(query) { rs, _ ->
            ProductStatsDto(
                productName = rs.getString("product_name"),
                productQuantity = rs.getLong("product_quantity"),
                mostRecent = rs.getTimestamp("most_recent").toLocalDateTime(),
            )
        }
    }
}
