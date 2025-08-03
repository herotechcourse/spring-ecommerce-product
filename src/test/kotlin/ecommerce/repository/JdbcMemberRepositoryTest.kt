package ecommerce.repository

import ecommerce.model.Member
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class JdbcMemberRepositoryTest {
    private lateinit var jdbcMemberRepository: MemberRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcMemberRepository = JdbcMemberRepository(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE IF EXISTS cart_items")
        jdbcTemplate.execute("DROP TABLE members IF EXISTS")
        jdbcTemplate.execute(
            """
                    CREATE TABLE members (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name     VARCHAR(50) DEFAULT '',
                email VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(50) DEFAULT 'USER'
            )
            """.trimIndent(),
        )

        jdbcTemplate.batchUpdate(
            "INSERT INTO members(email, password) VALUES (?, ?)",
            members,
            members.size,
        ) { ps, member ->
            ps.setString(1, member.email)
            ps.setString(2, member.password)
        }
    }

    @Test
    fun create() {
        val newMember =
            Member(
                email = "user@email.de",
                password = "1234",
            )
        jdbcMemberRepository.create(newMember)

        assertNotNull(jdbcMemberRepository.findByEmail(newMember.email))
    }

    @Test
    fun findByEmail() {
        assertNotNull(jdbcMemberRepository.findByEmail(members[0].email))
    }

    @Test
    fun existsByEmail() {
        Assertions.assertThat(jdbcMemberRepository.existsByEmail(members[0].email)).isTrue()
    }

    companion object {
        val members =
            listOf(
                Member(
                    id = 1L,
                    email = "u@email.com",
                    password = "1234",
                ),
                Member(
                    id = 2L,
                    email = "u2@mail.ru",
                    password = "1234",
                ),
            )
    }
}
