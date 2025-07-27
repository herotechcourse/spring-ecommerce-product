package ecommerce.repository

import ecommerce.dto.MemberRequest
import ecommerce.entity.Member
import ecommerce.helper.JdbcHelper
import ecommerce.sql.MemberConstsSQL
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

@Repository
class MemberRepository(private val jdbcTemplate: JdbcTemplate) {
    private val logger: Logger = LoggerFactory.getLogger(MemberRepository::class.java)
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
        val existing = jdbcTemplate.queryForObject(sql, Int::class.java, email)
        return existing != null && existing > 0
    }

    fun insert(request: MemberRequest): Long? {
        val sql = MemberConstsSQL.INSERT.trimIndent()
        return JdbcHelper.insertAndReturnKey(jdbcTemplate, sql, ::prepareInsertStatement, request)
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
        val result =
            jdbcTemplate.query(
                sql,
                PreparedStatementSetter { ps ->
                    ps.setLong(1, id)
                },
                rowMapper,
            )

        return when (result.size) {
            0 -> null
            1 -> result.first()
            else -> {
                logger.warn("Multiple members found with the same id $id (count=${result.size})")
                null
            }
        }
    }

    fun findByEmail(email: String): Member? {
        val sql = MemberConstsSQL.SELECT_BY_EMAIL
        val result =
            jdbcTemplate.query(
                sql,
                PreparedStatementSetter { ps ->
                    ps.setString(1, email)
                },
                rowMapper,
            )

        return when (result.size) {
            0 -> null
            1 -> result.first()
            else -> {
                logger.warn("Multiple members found with the same email: $email")
                null
            }
        }
    }

    fun matches(
        actual: String,
        expected: String,
    ): Boolean {
        return actual == expected
    }
//    fun deleteById(id: Long): Boolean
}
