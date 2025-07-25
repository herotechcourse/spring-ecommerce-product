package ecommerce.auth.store

import ecommerce.auth.data.Member
import ecommerce.auth.data.MemberRequest
import ecommerce.auth.sql.MemberConstsSQL
import ecommerce.helper.JdbcHelper.insertAndReturnKey
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.collections.first

@Repository
class MemberStore(private val jdbcTemplate: JdbcTemplate) {
    private val rowMapper =
        RowMapper<Member> { rs: ResultSet, _ ->
            Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
            )
        }

    fun existsByEmail(email: String): Boolean {
        val sql = MemberConstsSQL.COUNT_BY_EMAIL
        val found = jdbcTemplate.queryForObject(sql, Int::class.java, email)
        return found != null && found > 0
    }

    fun insertWithKeyholder(request: MemberRequest): Long {
        require(!existsByEmail(request.email)) // TODO message? {}
        val sql = MemberConstsSQL.INSERT.trimIndent()
        val id = insertAndReturnKey(jdbcTemplate, sql, ::prepareInsertStatement, request)
        return requireNotNull(id) {
            "Failed to retrieve generated ID after inserting product '${request.email}'. " +
                "Database key generation failed."
        }
    }

    private fun prepareInsertStatement(
        connection: Connection,
        sql: String,
        request: MemberRequest,
    ): PreparedStatement {
        return connection.prepareStatement(sql, arrayOf("id")).apply {
            setString(1, request.email)
            setString(2, request.password)
        }
    }

    fun findById(id: Long): Member? {
        val sql = MemberConstsSQL.SELECT_BY_ID
        val result = jdbcTemplate.query(sql, arrayOf(id), rowMapper)

        return when (result.size) {
            0 -> null
            1 -> result.first()
            else -> {
                throw IllegalStateException(
                    "Data integrity violation: found ${result.size} members with same id $id. " +
                        "This should never happen with a proper primary key constraint.",
                )
            }
        }
    }

//    fun save(member: Member): Member
//
//    fun findByEmail(email: String): Member?
//
//    fun existsByEmail(email: String): Boolean
//
//    fun deleteById(id: Long): Boolean
}
