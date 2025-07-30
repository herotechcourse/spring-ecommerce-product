package ecommerce.repository

import ecommerce.domain.Cart
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Timestamp

@Repository
class CartRepository(private val jdbcTemplate: JdbcTemplate) {
    private val cartMapper =
        RowMapper<Cart> { rs: ResultSet, _ ->
            Cart(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getTimestamp("created_at").toLocalDateTime(),
            )
        }

    fun findByMemberId(memberId: Long): Cart? {
        val sql = "SELECT * FROM carts c WHERE member_id=?"
        return jdbcTemplate.query(sql, cartMapper, memberId).firstOrNull()
    }

    fun create(cart: Cart) {
        val sql = "insert into carts (member_id, created_at) values (?, ?)"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setLong(1, cart.memberId)
            ps.setTimestamp(2, Timestamp.valueOf(cart.createdAt))
            ps
        }, keyHolder)

        cart.id = keyHolder.key?.toLong() ?: throw IllegalArgumentException("Failed to retrieve generated ID for cart.")
    }
}
