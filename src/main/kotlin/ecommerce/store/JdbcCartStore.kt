package ecommerce.store

import ecommerce.model.Cart
import ecommerce.model.CartItem
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcCartStore(private val db: JdbcTemplate) : CartStore {
    private val cartItemRowMapper =
        RowMapper<CartItem> { rs: ResultSet, _ ->
            CartItem(
                id = rs.getLong("id"),
                cartId = rs.getLong("cart_id"),
                productId = rs.getLong("product_id"),
                quantity = rs.getInt("quantity"),
                addedAt = rs.getTimestamp("added_at").toLocalDateTime(),
            )
        }

    override fun findCartByMemberId(memberId: Long): Cart? {
        val cartRow = db.queryForList("SELECT * FROM cart WHERE member_id = ?", memberId)
        if (cartRow.isEmpty()) return null
        val cartId = (cartRow[0]["id"] as Number).toLong()
        val items = getItems(cartId)
        return Cart(id = cartId, memberId = memberId, items = items)
    }

    override fun createCart(memberId: Long): Cart {
        val keyHolder = GeneratedKeyHolder()
        db.update({ conn ->
            conn.prepareStatement("INSERT INTO cart (member_id) VALUES (?)", arrayOf("id")).apply {
                setLong(1, memberId)
            }
        }, keyHolder)
        return Cart(id = keyHolder.key?.toLong() ?: throw IllegalStateException("No ID"), memberId = memberId)
    }

    override fun addCartItem(
        cartId: Long,
        productId: Long,
        quantity: Int,
    ): CartItem {
        db.update(
            """
            MERGE INTO cart_item (cart_id, product_id, quantity)
            KEY (cart_id, product_id)
            VALUES (?, ?, ?)
            """.trimIndent(),
            cartId,
            productId,
            quantity,
        )
        val item =
            db.queryForObject(
                "SELECT * FROM cart_item WHERE cart_id = ? AND product_id = ?",
                cartItemRowMapper,
                cartId,
                productId,
            )
        return item!!
    }

    override fun removeCartItem(
        cartId: Long,
        productId: Long,
    ): Boolean {
        return db.update(
            "DELETE FROM cart_item WHERE cart_id = ? AND product_id = ?",
            cartId, productId,
        ) > 0
    }

    override fun clearCart(cartId: Long) {
        db.update("DELETE FROM cart_item WHERE cart_id = ?", cartId)
    }

    override fun getItems(cartId: Long): List<CartItem> {
        return db.query("SELECT * FROM cart_item WHERE cart_id = ?", cartItemRowMapper, cartId)
    }
}
