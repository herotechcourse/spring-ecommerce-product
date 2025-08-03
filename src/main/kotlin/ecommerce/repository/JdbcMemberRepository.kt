package ecommerce.repository

import ecommerce.model.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class JdbcMemberRepository(private val jdbcTemplate: JdbcTemplate) : MemberRepository {
    private val rowMapper =
        RowMapper<Member> { rs, _ ->
            Member(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                email = rs.getString("email"),
                password = rs.getString("password"),
                role = rs.getString("role"),
            )
        }

    override fun create(member: Member): Long? {
        val nameToInsert = member.name.ifBlank { member.email }

        jdbcTemplate.update(
            "INSERT INTO members (name, email, password, role) VALUES (?, ?, ?, ?)",
            nameToInsert,
            member.email,
            member.password,
            member.role,
        )
        return jdbcTemplate.queryForObject("SELECT MAX(id) FROM members", Long::class.java)
    }

    override fun findByEmail(email: String): Member? {
        return jdbcTemplate.query(
            "SELECT * FROM members WHERE email = ?",
            rowMapper,
            email,
        ).firstOrNull()
    }

    override fun existsByEmail(email: String): Boolean {
        val row =
            jdbcTemplate.query(
                "SELECT * FROM members WHERE email = ?",
                { rs, _ -> rs.getLong("id") },
                email,
            )
        return row.isNotEmpty()
    }
}
