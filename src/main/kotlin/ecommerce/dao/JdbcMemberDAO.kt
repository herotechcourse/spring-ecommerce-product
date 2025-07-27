package ecommerce.dao

import ecommerce.model.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcMemberDAO(private val db: JdbcTemplate) : MemberDAO {
    private val memberRowMapper =
        RowMapper<Member> { rs: ResultSet, _ ->
            Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
            )
        }

    override fun insert(member: Member): Long {
        val sql = "INSERT INTO member (email, password) VALUES (?, ?)"
        val keyHolder = GeneratedKeyHolder()

        db.update({ connection ->
            connection.prepareStatement(sql, arrayOf("id")).apply {
                setString(1, member.email)
                setString(2, member.password)
            }
        }, keyHolder)
        return keyHolder.key?.toLong() ?: throw IllegalStateException("insert - Failed to retrieve ID")
    }

    override fun findByEmail(email: String): Member? {
        try {
            return db.queryForObject(
                "SELECT id, email, password FROM member WHERE email = ?",
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
                "SELECT id, email, password FROM member WHERE id = ?",
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
