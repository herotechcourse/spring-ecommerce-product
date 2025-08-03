package ecommerce.repository

import ecommerce.model.Member
import ecommerce.model.MemberRole
import org.springframework.dao.EmptyResultDataAccessException
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
            name = rs.getString("name"),
        )
    }

    fun create(member: Member): Member {
        val sql = "insert into members (id, email, password, role, name) VALUES (? ,? ,? ,?, ?)"
        jdbcTemplate.update(sql, member.id.toString(), member.email, member.password, member.role.name, member.name)
        return member
    }

    fun findByEmail(email: String): Member? {
        val sql = "select * from members where email = ?"
        return jdbcTemplate.query(sql, memberRowMapper, email).firstOrNull()
    }

    fun findById(id: UUID): Member? {
        val sql = "select * from members where id = ?"
        return try {
            jdbcTemplate.query(sql, memberRowMapper, id.toString()).firstOrNull()
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun findByIds(ids: List<UUID>): List<Member> {
        if (ids.isEmpty()) return emptyList()
        val inSql = ids.joinToString(",") { "'$it'" }
        val sql = "SELECT id, email, password, name, role FROM members WHERE id IN ($inSql)"
        return jdbcTemplate.query(sql, memberRowMapper)
    }
}
