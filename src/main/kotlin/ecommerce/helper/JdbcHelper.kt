package ecommerce.helper

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Connection
import java.sql.PreparedStatement

object JdbcHelper {
    fun <Res> insertAndReturnKey(
        jdbcTemplate: JdbcTemplate,
        sql: String,
        bind: (Connection, String, Res) -> PreparedStatement,
        request: Res,
    ): Long? {
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ conn ->
            bind(conn, sql, request)
        }, keyHolder)
        return keyHolder.key?.toLong()
    }
}
