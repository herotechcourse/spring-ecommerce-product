package ecommerce.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class CartItemRepository(private val jdbcTemplate: JdbcTemplate) {
    fun addProductToCart(
        productId: Long,
        cartId: Long,
        quantity: Int,
    ): Long {
        val insert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_items")
                .usingGeneratedKeyColumns("id")

        val productMap =
            mapOf(
                "cart_id" to cartId,
                "product_id" to productId,
                "quantity" to quantity,
            )
        return insert.executeAndReturnKey(productMap).toLong()
    }

    fun deleteProductFromCart(cartItemId: Long): Boolean {
        return jdbcTemplate.update("DELETE FROM cart_items WHERE id =?", cartItemId) > 0
    }
}
