package ecommerce.repository

import ecommerce.model.Member
import ecommerce.model.MemberRole
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID

@Repository
class MemberRepository(private val jdbcTemplate: JdbcTemplate) {
    private val memberRowMapper: (ResultSet, Int) -> Member = { rs, _ ->
        Member(
            id = UUID.fromString(rs.getString("id")),
            email = rs.getString("email"),
            password = rs.getString("password"),
            role = MemberRole.valueOf(rs.getString("role")),
        )
    }

    fun create(member: Member): Member {
        val sql = "insert into members (id, email, password, role) VALUES (? ,? ,? ,?)"
        jdbcTemplate.update(sql, member.id.toString(), member.email, member.password, member.role.name)
        return member
    }

    fun findByEmail(email: String): Member? {
        val sql = "select * from members where email = ?"
        return jdbcTemplate.query(sql, memberRowMapper, email).firstOrNull()
    }

    fun findById(id: UUID): Member? {
        val sql = "select * from members where id = ?"
        return jdbcTemplate.query(sql, memberRowMapper, id.toString()).firstOrNull()
    }
}
