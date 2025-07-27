package ecommerce.repository

import ecommerce.dto.CartItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CartRepository(private val jdbcTemplate: JdbcTemplate) {
    fun add(
        memberId: Long,
        productId: Long,
    ) {
        val sql =
            """
            INSERT INTO CART (member_id, product_id, quantity)
            VALUES (?, ?, 1)
            ON DUPLICATE KEY UPDATE quantity = quantity + 1
            """.trimIndent()
        jdbcTemplate.update(sql, memberId, productId)
    }

    fun remove(
        memberId: Long,
        productId: Long,
    ) {
        val sql = "DELETE FROM CART WHERE member_id = ? AND product_id = ?"
        jdbcTemplate.update(sql, memberId, productId)
    }

    fun getCartItems(memberId: Long): List<CartItem> {
        val sql =
            """
            SELECT c.product_id, p.name, p.price, c.quantity
            FROM CART c
            JOIN PRODUCTS p ON c.product_id = p.id
            WHERE c.member_id = ?
            """.trimIndent()

        return jdbcTemplate.query(sql, { rs, _ ->
            CartItem(
                productId = rs.getLong("product_id"),
                name = rs.getString("name"),
                price = rs.getInt("price"),
                quantity = rs.getInt("quantity"),
            )
        }, memberId)
    }
}
