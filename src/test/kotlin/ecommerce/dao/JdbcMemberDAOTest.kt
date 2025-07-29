package ecommerce.dao

import ecommerce.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class JdbcMemberDAOTest {
    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var jdbcMemberDAO: JdbcMemberDAO

    @BeforeEach
    fun setUp() {
        jdbcMemberDAO = JdbcMemberDAO(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE member CASCADE")
        jdbcTemplate.execute(
            """CREATE TABLE member
            (
                id       LONG         NOT NULL AUTO_INCREMENT,
                email    VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role     VARCHAR(255),
                PRIMARY KEY (id)
            );""",
        )

        val query =
            """INSERT INTO member (email, password, role) VALUES ( 'san@htc.com', 'san1234', 'admin');
            INSERT INTO member (email, password, role) VALUES ( 'dan@htc.com', 'dan1234', 'admin');
            INSERT INTO member (email, password) VALUES ( 'ann@htc.com', 'ann1234');
            INSERT INTO member (email, password) VALUES ( 'min@htc.com', 'min1234');"""
        jdbcTemplate.batchUpdate(query)
    }

    @Test
    fun insert() {
        val member = Member(email = "test@test.com", password = "test1234")
        val id = jdbcMemberDAO.insert(member)
        val target = jdbcMemberDAO.findById(id)
        assertThat(target?.email).isEqualTo(member.email)
        assertThat(target?.password).isEqualTo(member.password)
    }

    @Test
    fun findByEmail() {
        val email = "san@htc.com"
        val member = jdbcMemberDAO.findByEmail(email)
        val expected = 1L
        assertThat(member?.id).isEqualTo(expected)
    }

    @Test
    fun findById() {
        val id = 1L
        val member = jdbcMemberDAO.findById(id)
        val expected = "san@htc.com"
        assertThat(member?.email).isEqualTo(expected)
    }

    @Test
    fun `existsByEmail() - return false if email does not exist`() {
        val target = "test@test.com"
        val actual = jdbcMemberDAO.existsByEmail(target)
        assertThat(actual).isFalse
    }

    @Test
    fun `existsByEmail() - return true if email exists`() {
        val target = "san@htc.com"
        val actual = jdbcMemberDAO.existsByEmail(target)
        assertThat(actual).isTrue
    }
}
