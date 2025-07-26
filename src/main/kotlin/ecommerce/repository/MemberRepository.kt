package ecommerce.repository

import ecommerce.model.Member
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
            )
        }

    fun insert(
        member: Member,
    ): Member {
        val id = index.getAndIncrement()
        val sql =
            """
            INSERT INTO members ( id, email, password )
            VALUES (?, ?, ?)
            """.trimIndent()

        jdbcTemplate.update(sql, id, member.email, member.password)
        return member.copy(id = id)
    }

    fun findAll(): List<Member> {
        val sql = "SELECT id, email, password FROM members"
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun findByEmail(email: String): Member? {
        val sql = "SELECT id, email, password FROM members WHERE email = ?"
        return jdbcTemplate.query(sql, rowMapper, email).firstOrNull()
    }

    fun isEmptyOrNull(): Boolean {
        return count() == 0
    }

    fun count(): Int? {
        val sql = "SELECT count(*) FROM members"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    operator fun get(email: String): Member? {
        val sql = "SELECT id, email, password FROM members WHERE email = ?"
        return jdbcTemplate.query(sql, rowMapper, email).firstOrNull()
    }
}
