package ecommerce.dao

import ecommerce.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = ["/sql/member.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
)
class JdbcMemberDaoTest(
    @Autowired private val jdbcMemberDao: JdbcMemberDao,
) {
    @Test
    fun insert() {
        val member = Member(email = "test@test.com", password = "test1234")
        val id = jdbcMemberDao.insert(member)
        val target = jdbcMemberDao.findById(id)
        assertThat(target?.email).isEqualTo(member.email)
        assertThat(target?.password).isEqualTo(member.password)
    }

    @Test
    fun findByEmail() {
        val email = "san@htc.com"
        val member = jdbcMemberDao.findByEmail(email)
        val expected = 1L
        assertThat(member?.id).isEqualTo(expected)
    }

    @Test
    fun findById() {
        val id = 1L
        val member = jdbcMemberDao.findById(id)
        val expected = "san@htc.com"
        assertThat(member?.email).isEqualTo(expected)
    }

    @Test
    fun `existsByEmail() - return false if email does not exist`() {
        val target = "test@test.com"
        val actual = jdbcMemberDao.existsByEmail(target)
        assertThat(actual).isFalse
    }

    @Test
    fun `existsByEmail() - return true if email exists`() {
        val target = "san@htc.com"
        val actual = jdbcMemberDao.existsByEmail(target)
        assertThat(actual).isTrue
    }
}
