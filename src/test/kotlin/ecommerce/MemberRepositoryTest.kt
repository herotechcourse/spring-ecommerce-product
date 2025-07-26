package ecommerce

import ecommerce.member.domain.Member
import ecommerce.member.repository.MemberRepository
import jakarta.validation.ValidationException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertFailsWith

@JdbcTest
@Sql("/schema.sql")
class MemberRepositoryTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var repository: MemberRepository

    @BeforeEach
    fun setUp() {
        repository = MemberRepository(jdbcTemplate)
    }

    @Test
    fun `insert should save member and set id`() {
        val member =
            Member(
                id = null,
                email = "example@gmail.com",
                password = "password123qwerty",
                role = "USER",
            )

        repository.insert(member)

        assertNotNull(member.id)
        val savedMember = repository.findByEmail("example@gmail.com")
        assertNotNull(savedMember)
        assertEquals(member.id, savedMember.id)
        assertEquals("example@gmail.com", savedMember.email)
        assertEquals("password123qwerty", savedMember.password)
        assertEquals("USER", savedMember.role)
    }

    @Test
    fun `insert should throw ValidationException for duplicate email`() {
        val member1 =
            Member(
                id = null,
                email = "example@gmail.com",
                password = "password123qwerty",
                role = "USER",
            )
        repository.insert(member1)

        val member2 =
            Member(
                id = null,
                email = "example@gmail.com",
                password = "password456",
                role = "USER",
            )

        assertFailsWith<ValidationException> {
            repository.insert(member2)
        }
    }

    @Test
    fun `findByEmail should return member when email exists`() {
        val member =
            Member(
                id = null,
                email = "example@gmail.com",
                password = "password123qwerty",
                role = "USER",
            )
        repository.insert(member)

        val foundMember = repository.findByEmail("example@gmail.com")

        assertNotNull(foundMember)
        assertEquals(member.id, foundMember.id)
        assertEquals("example@gmail.com", foundMember.email)
        assertEquals("password123qwerty", foundMember.password)
        assertEquals("USER", foundMember.role)
    }

    @Test
    fun `findByEmail should return null when email does not exist`() {
        val foundMember = repository.findByEmail("nonexistent@gmail.com")

        assertNull(foundMember)
    }

    @Test
    fun `existsByEmail should return true when email exists`() {
        val member =
            Member(
                id = null,
                email = "example@gmail.com",
                password = "password123qwerty",
                role = "USER",
            )
        repository.insert(member)

        val exists = repository.existsByEmail("example@gmail.com")

        assertTrue(exists)
    }

    @Test
    fun `existsByEmail should return false when email does not exist`() {
        val exists = repository.existsByEmail("nonexistent@gmail.com")

        assertEquals(false, exists)
    }

    @AfterEach
    fun tearDown() {
        jdbcTemplate.execute("DELETE FROM CART_ITEMS")
        jdbcTemplate.execute("DELETE FROM PRODUCTS")
        jdbcTemplate.execute("DELETE FROM MEMBERS")
        jdbcTemplate.execute("ALTER TABLE MEMBERS ALTER COLUMN id RESTART WITH 1")
        jdbcTemplate.execute("ALTER TABLE PRODUCTS ALTER COLUMN id RESTART WITH 1")
    }
}
