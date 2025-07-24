package ecommerce.repository

import ecommerce.model.Cart
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class CartRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    private val cartRowMapper =
        RowMapper<Cart> { rs, _ ->
            Cart(
                rs.getLong("id"),
                rs.getLong("member_id"),
            )
        }

    fun createCart(memberId: Long): Long {
        // Implement the logic to create a new cart for a member
        val insert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart")
                .usingGeneratedKeyColumns("id")
        val parameters = mapOf("member_id" to memberId)
        // Return the generated cart ID
        return insert.executeAndReturnKey(parameters).toLong()
    }

    fun findCartByMemberId(memberId: Long): Cart? {
        // Implement the logic to find the cart ID for a given member
        val sql = "SELECT * FROM cart WHERE member_id = ?"
        return try {
            jdbcTemplate.queryForObject(sql, cartRowMapper, memberId)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }
}
