package ecommerce.repository

import ecommerce.model.Member
import ecommerce.model.Role
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.concurrent.atomic.AtomicLong

@Repository
class MemberRepository(private val jdbcTemplate: JdbcTemplate) {
    private val index = AtomicLong(1)

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
        member: Member,
    ): Member {
        val id = index.getAndIncrement()
        val sql =
            """
            INSERT INTO members ( id, email, password, role )
            VALUES (?, ?, ?, ?)
            """.trimIndent()

        jdbcTemplate.update(sql, id, member.email, member.password, member.role.toString())
        return member.copy(id = id)
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
