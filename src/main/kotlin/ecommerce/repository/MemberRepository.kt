package ecommerce.repository

import ecommerce.model.Member
import io.jsonwebtoken.security.Password
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class MemberRepository(private val jdbcTemplate: JdbcTemplate) {
    private val memberRowMapper =
        RowMapper { rs, _ ->
            Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
            )
        }

    fun registerMember(member: Member): Boolean {
        val sql = "INSERT INTO members (email, password, role) VALUES (?, ?, ?)"
        val rowsAffected = jdbcTemplate.update(sql, member.email, member.password, member.role)
        return rowsAffected > 0
    }

    fun existsByEmail(email: String): Boolean {
        val sql = "SELECT member FROM members where email = ?"
        val found = jdbcTemplate.queryForObject(sql, Member::class.java, email)
        return found != null
    }

    fun findMember(member: Member): Boolean {
        val sql = "SELECT member FROM members (email, password) VALUES (?, ?, ?)"
        val found = jdbcTemplate.queryForObject(sql, Member::class.java, member.email, member.password, member.role)
        return found != null
    }
}
