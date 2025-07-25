package ecommerce.repository

import ecommerce.model.Member
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class MemberRepository(private val db: JdbcClient) {
    private val memberRowMapper =
        RowMapper<Member> { rs: ResultSet, _ ->
            Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
            )
        }

    fun save(member: Member): Long? {
        val keyHolder = GeneratedKeyHolder()
        val sql = "INSERT INTO members (email, password) VALUES (?,?)"

        db.sql(sql)
            .params(member.email, member.password)
            .update(keyHolder)

        return keyHolder.key?.toLong()
    }

    fun existsByEmail(email: String): Boolean {
        return db
            .sql("SELECT COUNT(*) FROM members WHERE email =?")
            .param(email)
            .query(Int::class.java)
            .single() > 0
    }

    fun findByEmail(email: String): Member? {
        return db
            .sql("SELECT * FROM members WHERE email = ?")
            .param(email)
            .query { rs, _ ->
                Member(
                    id = rs.getLong("id"),
                    email = rs.getString("email"),
                    password = rs.getString("password"),
                    role = rs.getString("role"),
                )
            }
            .optional()
            .orElse(null)
    }
}
