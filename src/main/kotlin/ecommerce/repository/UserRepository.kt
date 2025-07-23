package ecommerce.repository

import ecommerce.dto.UserDTO
import ecommerce.mapper.UserRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val userRowMapper: UserRowMapper,
) {
    fun create(user: UserDTO): Long {
        val insert =
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id")

        val parameters =
            mapOf(
                "email" to user.email,
                "password" to user.password,
                "name" to user.name,
                "role" to user.role,
            )
        return insert.executeAndReturnKey(parameters).toLong()
    }

    fun findByEmailAndPassword(
        email: String,
        password: String,
    ): UserDTO? {
        val sql = "select email, name, role from users where email = ? and password = ?"
        val res = jdbcTemplate.query(sql, userRowMapper, email, password)
        return res.firstOrNull()
    }

    fun existsByEmail(email: String): Boolean {
        val sql = "select count(*) from users where email = ?"
        return jdbcTemplate.queryForObject(sql, Int::class.java, email)!! > 0
    }
}
