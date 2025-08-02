package ecommerce.repository

import ecommerce.dto.MemberDTO
import ecommerce.model.Role
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberRepositoryTest {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var repository: MemberRepository

    @BeforeEach
    fun setUp() {
        repository = MemberRepository(jdbcTemplate)
    }

    @Test
    fun `count should return correct number of members`() {
//        data.sql already has 2 members

        assertThat(repository.count()).isEqualTo(2)
    }

    @Test
    fun `findByEmail should return already existing member`() {
        val member1 = MemberDTO("member1@test.com", "1234", Role.ADMIN, "Jon")
        val member2 = MemberDTO("member2@test.com", "12345", Role.USER, "Ann")
        repository.insert(member1)
        repository.insert(member2)

        val result = repository.findByEmail("member1@test.com")
        assertThat(result).isNotNull()
        assertThat(result?.email).isEqualTo("member1@test.com")
    }
}
