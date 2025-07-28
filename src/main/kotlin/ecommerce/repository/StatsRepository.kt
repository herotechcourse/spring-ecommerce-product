package ecommerce.repository

import ecommerce.dto.MemberStatsResponse
import ecommerce.dto.ProductStatsResponse
import ecommerce.sql.SortOption
import ecommerce.sql.StateConstsSQL.ALL_ACTIVE_MEMBERS_TEMPLATE
import ecommerce.sql.StateConstsSQL.TOP_ADDED_PRODUCTS_TEMPLATE
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class StatsRepository(private val jdbcTemplate: JdbcTemplate) {
    fun findTopProducts(
        days: Int,
        limit: Int,
        sort: SortOption,
    ): List<ProductStatsResponse> {
        val sql = String.format(TOP_ADDED_PRODUCTS_TEMPLATE, sort.sql)
        return jdbcTemplate.query(
            sql,
            PreparedStatementSetter { ps ->
                ps.setInt(1, days)
                ps.setInt(2, limit)
            },
            topProductRowMapper,
        )
    }

    private val topProductRowMapper =
        RowMapper<ProductStatsResponse> { rs: ResultSet, _ ->
            ProductStatsResponse(
                productId = rs.getLong("product_id"),
                addCount = rs.getLong("add_count"),
                lastAdded = rs.getTimestamp("last_added_at").toLocalDateTime(),
            )
        }

    fun findAllActiveMembers(
        days: Int,
        sort: SortOption,
    ): List<MemberStatsResponse> {
        val sql = String.format(ALL_ACTIVE_MEMBERS_TEMPLATE, sort.sql)
        return jdbcTemplate.query(
            sql,
            PreparedStatementSetter { ps ->
                ps.setInt(1, -days)
            },
            activeMemberRowMapper,
        )
    }

    private val activeMemberRowMapper =
        RowMapper<MemberStatsResponse> { rs: ResultSet, _ ->
            MemberStatsResponse(
                memberId = rs.getLong("member_id"),
                email = rs.getString("email"),
            )
        }
}
