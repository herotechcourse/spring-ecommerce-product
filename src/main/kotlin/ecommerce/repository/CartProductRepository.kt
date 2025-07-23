package ecommerce.repository

import ecommerce.dto.cartProduct.CartProductDTO
import ecommerce.mapper.CartProductMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CartProductRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val cartProductMapper: CartProductMapper,
) {
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
        val sql = "delete from cart_products (product_id, user_id) values ?, ?, ?"
        return jdbcTemplate.update(sql, productID, cartID)
    }

    fun addProduct(
        cartID: Long,
        productID: Long,
    ): Int {
        val sql = "insert into cart_products (quantity, product_id, user_id) values ?, ?, ?"
        return jdbcTemplate.update(sql, 1, productID, cartID)
    }
}
