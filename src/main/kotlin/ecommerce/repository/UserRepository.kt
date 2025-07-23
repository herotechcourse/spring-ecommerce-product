package ecommerce.repository

import ecommerce.entity.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class UserRepository(private val jdbc: JdbcTemplate) {
    private val rowMapper =
        RowMapper<User> { rs, _ ->
            User(
                id = rs.getLong("id"),
                email = rs.getString("email"),
                password = rs.getString("password"),
                role = rs.getString("role"),
            )
        }

    fun getAll(): List<User> {
        return jdbc.query("SELECT * FROM users", rowMapper)
    }

    fun create(user: User): Long {
        jdbc.update(
            "INSERT INTO users (email, password, role) VALUES (?, ?, ?)",
            user.email,
            user.password,
            user.role,
        )
        return jdbc.queryForObject("SELECT MAX(id) FROM users", Long::class.java)!!
    }

    fun existsByEmail(email: String): Boolean {
        val count = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE email = ?", Int::class.java, email)
        return count != null && count > 0
    }

    fun getByEmail(email: String): User? {
        val users = jdbc.query("SELECT * FROM users WHERE email = ?", rowMapper, email)
        return users.firstOrNull()
    }
}
