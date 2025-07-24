package ecommerce.repository

import ecommerce.model.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class MemberRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    private val rowMapper = RowMapper<Member> { rs: ResultSet, _ ->
        Member(
            id = rs.getLong("id"),
            email = rs.getString("email"),
            password = rs.getString("password")
        )
    }

    fun save(member: Member): Member {
        jdbcTemplate.update(
            "INSERT INTO members (email, password) VALUES (?, ?)",
            member.email,
            member.password
        )
        val id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long::class.java)!!
        return member.copy(id = id)
    }

    fun findByEmail(email: String): Member? {
        val results = jdbcTemplate.query(
            "SELECT * FROM members WHERE email = ?",
            rowMapper,
            email
        )
        return results.firstOrNull()
    }

    fun findById(id: Long): Member? {
        val results = jdbcTemplate.query(
            "SELECT * FROM members WHERE id = ?",
            rowMapper,
            id
        )
        return results.firstOrNull()
    }
}
