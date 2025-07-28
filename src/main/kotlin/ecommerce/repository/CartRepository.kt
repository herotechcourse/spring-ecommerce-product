package ecommerce.repository

import ecommerce.dto.CartRequest
import ecommerce.entity.CartItem
import ecommerce.helper.JdbcHelper
import ecommerce.sql.CartConstsSQL
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class CartRepository(private val jdbcTemplate: JdbcTemplate) {
    private val rowMapper =
        RowMapper<CartItem> { rs: ResultSet, _ ->
            CartItem(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
            )
        }

    fun existsByMemberId(memberId: Long): Boolean {
        val sql = CartConstsSQL.COUNT_BY_MEMBER
        val existing = jdbcTemplate.queryForObject(sql, Int::class.java, memberId)
        return existing != null && existing > 0
    }

    fun selectByMemberId(memberId: Long): List<CartItem> {
        val sql = CartConstsSQL.SELECT_BY_MEMBER.trimIndent()
        val items: List<CartItem> =
            jdbcTemplate.query(
                sql,
                rowMapper,
                memberId,
            )
        return items
    }

    fun deleteByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): Int {
        val sql = CartConstsSQL.DELETE_BY_MEMBER_AND_PRODUCT.trimIndent()
        return jdbcTemplate.update(sql, memberId, productId)
    }

    fun insert(
        memberId: Long,
        request: CartRequest,
    ): Long? {
        val sql = CartConstsSQL.INSERT.trimIndent()
        return JdbcHelper.insertAndReturnKey(
            jdbcTemplate,
            sql,
            request,
        ) { connection, sql, req ->
            connection.prepareStatement(sql, arrayOf("id")).apply {
                setLong(1, memberId)
                setLong(2, req.productId)
                setInt(3, req.quantity)
            }
        }
    }

    fun findByItemId(id: Long): CartItem? {
        val sql = CartConstsSQL.SELECT_BY_ID.trimIndent()
        return jdbcTemplate.query(sql, rowMapper, id)
            .firstOrNull()
    }
}
