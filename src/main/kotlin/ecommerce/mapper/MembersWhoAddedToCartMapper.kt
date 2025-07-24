package ecommerce.mapper

import ecommerce.dto.cartStatistics.MembersWhoAddedToCartDTO
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class MembersWhoAddedToCartMapper : RowMapper<MembersWhoAddedToCartDTO> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): MembersWhoAddedToCartDTO {
        return MembersWhoAddedToCartDTO(
            name = rs.getString("name"),
            id = rs.getLong("id"),
            email = rs.getString("email"),
        )
    }
}
