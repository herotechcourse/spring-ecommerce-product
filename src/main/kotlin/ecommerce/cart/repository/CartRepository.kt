package ecommerce.cart.repository

import ecommerce.cart.domain.CartItem
import jakarta.validation.ValidationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class CartRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun insert(cartItem: CartItem) {
        if (existsByMemberIdAndProductId(cartItem.memberId, cartItem.productId)) {
            throw ValidationException("Product is already in the cart")
        }
        val simpleJdbcInsert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("CART_ITEMS")
                .usingGeneratedKeyColumns("id")
        val parameters =
            mapOf(
                "member_id" to cartItem.memberId,
                "product_id" to cartItem.productId,
            )
        val id = simpleJdbcInsert.executeAndReturnKey(parameters).toLong()
        cartItem.id = id
    }

    fun findByMemberId(memberId: Long): List<CartItem> {
        val sql = "SELECT * FROM CART_ITEMS WHERE member_id = ?"
        return jdbcTemplate.query(sql, { rs, _ ->
            CartItem(
                id = rs.getLong("id"),
                memberId = rs.getLong("member_id"),
                productId = rs.getLong("product_id"),
            )
        }, memberId)
    }

    fun deleteByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ) {
        val sql = "DELETE FROM CART_ITEMS WHERE member_id = ? AND product_id = ?"
        jdbcTemplate.update(sql, memberId, productId)
    }

    fun existsByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): Boolean {
        val sql = "SELECT COUNT(*) FROM CART_ITEMS WHERE member_id = ? AND product_id = ?"
        val count = jdbcTemplate.queryForObject(sql, Int::class.java, memberId, productId) ?: 0
        return count > 0
    }
}
