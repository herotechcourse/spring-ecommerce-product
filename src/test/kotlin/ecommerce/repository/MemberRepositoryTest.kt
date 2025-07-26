package ecommerce.repository

import ecommerce.model.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
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
        val member1 = Member( 1,"member1@test.com", "1234")
        val member2 = Member( 2,"member2@test.com", "12345")
        repository.insert(member1)
        repository.insert(member2)

        assertThat(repository.count()).isEqualTo(2)
    }

    @Test
    fun `findByEmail should return already existing member`() {
        val member1 = Member( 1,"member1@test.com", "1234")
        val member2 = Member( 2,"member2@test.com", "12345")
        repository.insert(member1)
        repository.insert(member2)

        val result = repository.findByEmail("member1@test.com")
        assertThat(result).isNotNull()
        assertThat(result?.email).isEqualTo("member1@test.com")
    }
}
