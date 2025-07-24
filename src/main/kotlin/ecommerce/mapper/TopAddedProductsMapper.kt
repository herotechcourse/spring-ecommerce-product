package ecommerce.mapper

import ecommerce.dto.cartStatistics.TopAddedProductsDTO
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class TopAddedProductsMapper : RowMapper<TopAddedProductsDTO> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): TopAddedProductsDTO {
        return TopAddedProductsDTO(
            name = rs.getString("name"),
            count = rs.getInt("count"),
            createdAt = rs.getString("created_at"),
        )
    }
}
