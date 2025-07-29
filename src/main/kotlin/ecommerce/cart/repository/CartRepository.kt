package ecommerce.cart.repository

import ecommerce.admin.dto.RecentMemberResponse
import ecommerce.admin.dto.TopProductResponse
import ecommerce.cart.domain.CartItem
import jakarta.validation.ValidationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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
                "added_at" to (cartItem.addedAt ?: LocalDateTime.now()),
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
        val sql = """
            SELECT EXISTS (
                SELECT 1 FROM CART_ITEMS WHERE member_id = ? AND product_id = ?
            )
        """
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, memberId, productId) ?: false
    }

    fun findTopProductsInLast30Days(): List<TopProductResponse> {
        val sql = """
            SELECT 
                p.name AS product_name,
                COUNT(*) AS add_count,
                MAX(ci.added_at) AS last_added_at
            FROM CART_ITEMS ci
            JOIN PRODUCTS p ON ci.product_id = p.id
            WHERE ci.added_at >= CURRENT_TIMESTAMP - 30 DAY
            GROUP BY p.id, p.name
            ORDER BY add_count DESC, last_added_at DESC
            LIMIT 5
        """
        return jdbcTemplate.query(sql) { rs, _ ->
            TopProductResponse(
                productName = rs.getString("product_name"),
                addCount = rs.getLong("add_count"),
                lastAddedAt = rs.getTimestamp("last_added_at").toLocalDateTime(),
            )
        }
    }

    fun findRecentMembersInLast7Days(): List<RecentMemberResponse> {
        val sql = """
            SELECT DISTINCT 
                m.id AS member_id,
                m.email
            FROM MEMBERS m
            JOIN CART_ITEMS ci ON m.id = ci.member_id
            WHERE ci.added_at >= CURRENT_TIMESTAMP - 7 DAY
        """
        return jdbcTemplate.query(sql) { rs, _ ->
            RecentMemberResponse(
                memberId = rs.getLong("member_id"),
                email = rs.getString("email"),
            )
        }
    }
}
