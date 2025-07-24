package ecommerce.mapper

import ecommerce.dto.user.MemberUserDTO
import ecommerce.enums.UserRole
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserRowMapper : RowMapper<MemberUserDTO> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): MemberUserDTO {
        return MemberUserDTO(
            id = rs.getLong("id"),
            email = rs.getString("email"),
            name = rs.getString("name"),
            role = UserRole.valueOf(rs.getString("role")),
            password = rs.getString("password"),
        )
    }
}
