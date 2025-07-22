package ecommerce.mapper

import ecommerce.dto.ProductDTO
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class ProductRowMapper : RowMapper<ProductDTO> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): ProductDTO {
        return ProductDTO(
            id = rs.getLong("id"),
            name = rs.getString("name"),
            price = rs.getDouble("price"),
            description = rs.getString("description"),
            imageUrl = rs.getString("image_url"),
        )
    }
}
