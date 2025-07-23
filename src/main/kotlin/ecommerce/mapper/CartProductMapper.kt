package ecommerce.mapper

import ecommerce.dto.cartProduct.CartProductDTO
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class CartProductMapper : RowMapper<CartProductDTO> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): CartProductDTO {
        return CartProductDTO(
            id = rs.getLong("id"),
            cartId = rs.getLong("cart_id"),
            productID = rs.getLong("product_id"),
            quantity = rs.getInt("quantity"),
        )
    }
}
