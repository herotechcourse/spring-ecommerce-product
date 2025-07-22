package ecommerce.repository

import ecommerce.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class UserRepositoryTest {
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        userRepository = UserRepository(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE IF EXISTS users")
        jdbcTemplate.execute(
            """
            CREATE TABLE users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                role VARCHAR(50) NOT NULL
            )
            """.trimIndent(),
        )

        jdbcTemplate.update(
            "INSERT INTO users (email, password_hash, role) VALUES (?, ?, ?)",
            "admin@example.com",
            "$2a$10$7Q0N9Fg56qE.L3HqRkDYBeLPDe/s3ZPaXL/3O1chLn5XwB1Qh7v2.",
            "ADMIN",
        )
        jdbcTemplate.update(
            "INSERT INTO users (email, password_hash, role) VALUES (?, ?, ?)",
            "user@example.com",
            "$2a$10\$P0hS6GH5z9TbG1vhGQKZtOj.4/ZuvKSh6Iz36mSHb.9uKYZ9a3Lty",
            "USER",
        )
    }

    @Test
    fun `should create user`() {
        val user = User(email = "newuser@example.com", passwordHash = "hashedpass", role = "USER")
        val id = userRepository.create(user)

        val createdUser = userRepository.getByEmail("newuser@example.com")
        assertThat(createdUser).isNotNull
        assertThat(createdUser?.id).isEqualTo(id)
        assertThat(createdUser?.email).isEqualTo("newuser@example.com")
    }

    @Test
    fun `should find user by email`() {
        val user = userRepository.getByEmail("admin@example.com")
        assertThat(user).isNotNull
        assertThat(user?.email).isEqualTo("admin@example.com")
    }

    @Test
    fun `should return all users`() {
        val users = userRepository.getAll()
        assertThat(users).hasSize(2)
    }

    @Test
    fun `should check existence by email`() {
        assertThat(userRepository.existsByEmail("admin@example.com")).isTrue
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse
    }
}
