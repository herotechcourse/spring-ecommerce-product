package ecommerce.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CartRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun createCartForUser(userId: Long) {
        val sql = "insert into cart (user_id) values ?"
        jdbcTemplate.update(sql, userId)
    }
}
