package ecommerce.repository

import ecommerce.dto.MemberDTO
import ecommerce.model.Member
import ecommerce.model.Role
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class MemberRepository(private val jdbcTemplate: JdbcTemplate) {
    private val rowMapper =
        RowMapper<Member> { rs: ResultSet, _ ->
            Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role")),
            )
        }

    fun insert(
        memberDTO: MemberDTO,
    ): Member {
        val keyHolder = GeneratedKeyHolder()

        val sql =
            """
            INSERT INTO members ( email, password, role )
            VALUES (?, ?, ?)
            """.trimIndent()

        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, arrayOf("id"))
            ps.setString(1, memberDTO.email)
            ps.setString(2, memberDTO.password)
            ps.setString(3, memberDTO.role.toString())
            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong() ?: throw IllegalStateException("Failed to retrieve ID")

        return memberDTO.toEntity(generatedId)
    }

    fun findById(id: Long): Member? {
        val sql = "SELECT id, email, password, role FROM members WHERE id = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun findByEmail(email: String): Member? {
        val sql = "SELECT id, email, password, role FROM members WHERE email = ?"
        return jdbcTemplate.query(sql, rowMapper, email).firstOrNull()
    }

    fun count(): Int? {
        val sql = "SELECT count(*) FROM members"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    operator fun get(email: String): Member? {
        val sql = "SELECT id, email, password, role FROM members WHERE email = ?"
        return jdbcTemplate.query(sql, rowMapper, email).firstOrNull()
    }
}
