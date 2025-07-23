package ecommerce.mapper

import ecommerce.dto.UserDTO
import ecommerce.enums.UserRole
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserRowMapper : RowMapper<UserDTO> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): UserDTO {
        return UserDTO(
            email = rs.getString("email"),
            name = rs.getString("name"),
            role = UserRole.valueOf(rs.getString("role")),
            password = rs.getString("password"),
        )
    }
}
