package ecommerce.repository

import ecommerce.domain.CartEvent
import ecommerce.dto.report.MemberCartActivityDto
import ecommerce.dto.report.ProductCartCountDto
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

@Repository
class CartEventRepository(private val jdbcTemplate: JdbcTemplate) {
    private val cartItemMapper =
        RowMapper<ProductCartCountDto> { rs: ResultSet, _ ->
            ProductCartCountDto(
                productId = rs.getLong("productId"),
                productName = rs.getString("productName"),
                addedCount = rs.getLong("addedCount"),
                lastAddedTime = rs.getTimestamp("lastAddedTime").toLocalDateTime(),
            )
        }

    private val memberCartActivityDTOMapper =
        RowMapper<MemberCartActivityDto> { rs: ResultSet, _ ->
            MemberCartActivityDto(
                memberId = rs.getLong("memberId"),
                userName = rs.getString("userName"),
                email = rs.getString("email"),
            )
        }

    fun save(cartEvent: CartEvent): CartEvent {
        val sql = "INSERT INTO cart_events (member_id, product_id, quantity_added, timestamp) VALUES (?, ?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setLong(1, cartEvent.memberId)
            ps.setLong(2, cartEvent.productId)
            ps.setInt(3, cartEvent.quantityAdded)
            ps.setObject(4, cartEvent.timestamp)
            ps
        }, keyHolder)

        cartEvent.id =
            keyHolder.key?.toLong() ?: throw IllegalStateException("Failed to retrieve generated ID for cart_event.")
        return cartEvent
    }

    fun findTop5MostAddedProductsInLast30Days(startDate: LocalDateTime): List<ProductCartCountDto> {
        val sql = """
            SELECT
                ce.product_id AS productId,
                p.name AS productName,
                COUNT(ce.id) AS addedCount,
                MAX(ce.timestamp) AS lastAddedTime
            FROM
                cart_events ce
            JOIN
                products p ON ce.product_id = p.id
            WHERE
                ce.timestamp >= ?
            GROUP BY
                ce.product_id, p.name
            ORDER BY
                addedCount DESC, lastAddedTime DESC
            LIMIT 5
        """

        return jdbcTemplate.query(sql, cartItemMapper, startDate)
    }

    fun findMembersWhoAddedItemsInLastDays(startDate: LocalDateTime): List<MemberCartActivityDto> {
        val sql = """
            SELECT DISTINCT
                m.user_id AS memberId,
                m.user_name AS userName,
                m.email AS email
            FROM
                cart_events ce
            JOIN
                members m ON ce.member_id = m.user_id
            WHERE
                ce.timestamp >= ?
            ORDER BY
                m.user_id ASC
        """
        return jdbcTemplate.query(sql, memberCartActivityDTOMapper, startDate)
    }
}
