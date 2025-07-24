package ecommerce.mapper

import ecommerce.dto.cart.CartDTO
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class CartRowMapper : RowMapper<CartDTO> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): CartDTO {
        return CartDTO(
            id = rs.getLong("id"),
            userId = rs.getLong("user_id"),
        )
    }
}
