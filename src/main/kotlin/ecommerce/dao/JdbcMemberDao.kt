package ecommerce.dao

import ecommerce.model.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcMemberDao(private val db: JdbcTemplate) : MemberDao {
    private val memberRowMapper =
        RowMapper<Member> { rs: ResultSet, _ ->
            Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
            )
        }

    override fun insert(member: Member): Long {
        val sql = "INSERT INTO member (email, password, role) VALUES (?, ?, ?)"
        val keyHolder = GeneratedKeyHolder()

        db.update({ connection ->
            connection.prepareStatement(sql, arrayOf("id")).apply {
                setString(1, member.email)
                setString(2, member.password)
                setString(3, member.role)
            }
        }, keyHolder)
        return keyHolder.key?.toLong() ?: throw IllegalStateException("insert - Failed to retrieve ID")
    }

    override fun findByEmail(email: String): Member? {
        try {
            return db.queryForObject(
                "SELECT id, email, password, role FROM member WHERE email = ?",
                memberRowMapper,
                email,
            )
        } catch (exception: Exception) {
            println("findByEmail(): " + exception.message)
            return null
        }
    }

    override fun findById(id: Long): Member? {
        try {
            return db.queryForObject(
                "SELECT id, email, password, role FROM member WHERE id = ?",
                memberRowMapper,
                id,
            )
        } catch (exception: Exception) {
            println("findById(): " + exception.message)
            return null
        }
    }

    override fun existsByEmail(email: String): Boolean {
        val sql = "SELECT COUNT(*) FROM member WHERE email = ?"
        val count = db.queryForObject(sql, Long::class.java, email)
        return count != null && count > 0
    }
}
