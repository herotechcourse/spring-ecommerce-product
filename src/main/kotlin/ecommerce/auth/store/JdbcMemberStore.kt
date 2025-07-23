package ecommerce.auth.store

import ecommerce.auth.model.Member
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcMemberStore(private val db: JdbcTemplate) : MemberStore {
    private val userRowMapper =
        RowMapper<Member> { rs: ResultSet, _ ->
            Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
            )
        }

    override fun createMember(member: Member): Member {
        val keyHolder = GeneratedKeyHolder()
        db.update({ connection ->
            connection.prepareStatement("INSERT INTO member (email, password) VALUES (?, ?)", arrayOf("id")).apply {
                setString(1, member.email)
                setString(2, member.password)
            }
        }, keyHolder)
        return member.copy(id = keyHolder.key?.toLong() ?: throw IllegalStateException("No ID returned"))
    }
}
