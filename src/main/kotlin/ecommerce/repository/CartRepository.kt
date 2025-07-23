package ecommerce.repository

import ecommerce.mapper.ProductRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CartRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val productRowMapper: ProductRowMapper,
) {
    fun createCartForUser(userId: Long) {
        val sql = "insert into cart (user_id) values ?"
        jdbcTemplate.update(sql, userId)
    }

    fun findMembersCart(userId: Long): Long? {
        val sql = "select * from cart where user_id = ?"
        return jdbcTemplate.queryForObject(sql, Long::class.javaObjectType, userId)
    }
}
