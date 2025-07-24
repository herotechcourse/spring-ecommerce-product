package ecommerce.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class CartRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
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
}
