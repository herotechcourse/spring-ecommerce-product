package ecommerce.service

import ecommerce.exception.DuplicateMemberEmailException
import ecommerce.model.Member
import ecommerce.repository.MemberRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MemberServiceTest {
    private lateinit var repository: MemberRepository
    private lateinit var service: MemberService

    @BeforeEach
    fun setUp() {
        repository = mockk()
        service = MemberService(repository)
    }

    @Test
    fun `should throw DuplicateMemberEmailException if email exists`() {
        val member = Member(email = "test@example.com", password = "pass")
        every { repository.existsByEmail(member.email) } returns true

        assertThrows<DuplicateMemberEmailException> {
            service.validateUniqueName(member)
        }
    }

    @Test
    fun `should pass silently if email is unique`() {
        val member = Member(email = "unique@example.com", password = "pass")
        every { repository.existsByEmail(member.email) } returns false

        assertThatCode {
            service.validateUniqueName(member)
        }.doesNotThrowAnyException()
    }

    @Test
    fun `should return member by email`() {
        val member = Member(email = "m@email.com", password = "1234")
        every { repository.findByEmail("m@email.com") } returns member

        val result = service.findByEmail("m@email.com")

        assertEquals(member, result)
    }

    @Test
    fun `should return true if email exists`() {
        every { repository.existsByEmail("admin@a.com") } returns true

        val result = service.existsByEmail("admin@a.com")

        assertTrue(result)
    }

    @Test
    fun `should call repository to create member`() {
        val member = Member(email = "new@a.com", password = "pw")
        every { repository.create(member) } returns 42L

        val id = service.create(member)

        assertEquals(42L, id)
        verify { repository.create(member) }
    }
}
