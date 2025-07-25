package ecommerce.repository

import ecommerce.domain.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement

@Repository
class MemberRepository(private val jdbcTemplate: JdbcTemplate) {
    private val memberRowMapper =
        RowMapper<Member> { rs: ResultSet, _ ->
            Member(
                rs.getLong("user_id"),
                rs.getString("user_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("role"),
            )
        }

    fun findByUserId(userId: Long): Member? {
        val sql = "select * from members where user_id = ?"
        return jdbcTemplate.query(sql, memberRowMapper, userId).firstOrNull()
    }

    fun findByEmail(email: String): Member? {
        val sql = "select * from members where email = ?"
        return jdbcTemplate.query(sql, memberRowMapper, email).firstOrNull()
    }

    fun create(member: Member) {
        val sql = "insert into members (user_name, email, password_hash, role) values (?, ?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, member.userName)
            ps.setString(2, member.email)
            ps.setString(3, member.passwordHash)
            ps.setString(4, member.role)
            ps
        }, keyHolder)

        member.userId =
            keyHolder.key?.toLong() ?: throw IllegalStateException("Failed to retrieve generated ID for member.")
    }

    fun update(
        userId: Long,
        member: Member,
    ) {
        val sql = "update members set user_name = ?, set email = ? where user_id = ?"
        jdbcTemplate.update(sql, member.userName, member.email, userId)
    }

    fun delete(userId: Long) {
        val sql = "delete from members where user_id = ?"
        jdbcTemplate.update(sql, userId)
    }
}
