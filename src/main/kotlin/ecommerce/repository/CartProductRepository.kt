package ecommerce.repository

import ecommerce.dto.cartProduct.CartProductDTO
import ecommerce.dto.cartProduct.CartProductResponseDTO
import ecommerce.mapper.CartProductMapper
import ecommerce.mapper.CartProductResponseMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class CartProductRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val cartProductMapper: CartProductMapper,
    private val cartProductResponseMapper: CartProductResponseMapper,
) {
    fun getCartProducts(cartID: Long): List<CartProductResponseDTO> {
        val sql =
            """
            select 
                p.id as product_id,
                p.name,
                p.description,
                p.price,
                p.image_url,
                cp.quantity
            from cart_products cp
            join products p ON cp.product_id = p.id
            where cp.cart_id = ?
            """.trimIndent()
        return jdbcTemplate.query(sql, cartProductResponseMapper, cartID)
    }

    fun findCartProduct(
        cartID: Long,
        productID: Long,
    ): CartProductDTO? {
        val sql = "select * from cart_products where cart_id = ? and product_id = ?"
        val res = jdbcTemplate.query(sql, cartProductMapper, cartID, productID)
        return res.firstOrNull()
    }

    fun updateProductQuantity(
        cartProductId: Long,
        quantity: Int,
    ): Int {
        val sql = "update cart_products set quantity = ? where id = ?"
        return jdbcTemplate.update(sql, quantity, cartProductId)
    }

    fun removeProduct(
        cartID: Long,
        productID: Long,
    ): Int {
        val sql = "DELETE FROM cart_products WHERE product_id = ? AND cart_id = ?"
        return jdbcTemplate.update(sql, productID, cartID)
    }

    fun addProduct(
        cartID: Long,
        productID: Long,
    ): Long {
        val insert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_products")
                .usingGeneratedKeyColumns("id")

        val parameters =
            mapOf(
                "quantity" to 1,
                "product_id" to productID,
                "cart_id" to cartID,
            )
        return insert.executeAndReturnKey(parameters).toLong()
    }
}
