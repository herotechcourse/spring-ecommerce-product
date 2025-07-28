package ecommerce.member.repository

import ecommerce.auth.exception.DuplicateMemberEmailException
import ecommerce.member.domain.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class MemberRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun insert(member: Member) {
        if (existsByEmail(member.email)) {
            throw DuplicateMemberEmailException("Email is already registered")
        }
        val simpleJdbcInsert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("MEMBERS")
                .usingGeneratedKeyColumns("id")
        val parameters =
            mapOf(
                "email" to member.email,
                "password" to member.password,
                "role" to member.role,
            )
        val id = simpleJdbcInsert.executeAndReturnKey(parameters).toLong()
        member.id = id
    }

    fun findByEmail(email: String): Member? {
        val sql = "SELECT * FROM MEMBERS WHERE email = ?"
        return jdbcTemplate.query(sql, { rs, _ ->
            Member(
                id = rs.getLong("id"),
                email = rs.getString("email"),
                password = rs.getString("password"),
                role = rs.getString("role"),
            )
        }, email).firstOrNull()
    }

    fun existsByEmail(email: String): Boolean {
        val sql = """
            SELECT EXISTS (
                SELECT 1 FROM MEMBERS WHERE email = ?
            )
        """
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, email) ?: false
    }
}
