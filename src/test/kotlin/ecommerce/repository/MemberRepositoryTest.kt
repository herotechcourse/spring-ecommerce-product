package ecommerce.repository

import ecommerce.model.Member
import ecommerce.model.MemberRole
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import java.util.UUID

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(MemberRepository::class)
class MemberRepositoryTest @Autowired constructor(
    private val memberRepository: MemberRepository)
{
    @Test
    fun `it should find a member by email`() {
        val memberId = UUID.randomUUID()
        val member = Member(
            id = memberId,
            email = "anotherTest@email.com",
            password = "AnotherSecure@123",
            role = MemberRole.ROLE_USER
        )
        memberRepository.create(member)
        val foundMember = memberRepository.findByEmail(member.email)
        assertThat(foundMember).isNotNull
        assertThat(foundMember?.id).isEqualTo(member.id)
        assertThat(foundMember?.email).isEqualTo(member.email)
        assertThat(foundMember?.password).isEqualTo(member.password)
        assertThat(foundMember?.role).isEqualTo(member.role)
    }

    @Test
    fun `it should find a member by id`() {
        val memberId = UUID.randomUUID()
        val member = Member(
            id = memberId,
            email = "testEmail@email.com",
            password = "Secure@123",
            role = MemberRole.ROLE_USER
        )
        memberRepository.create(member)
        val foundMember = memberRepository.findById(member.id)
        assertThat(foundMember).isNotNull
        assertThat(foundMember?.id).isEqualTo(member.id)
        assertThat(foundMember?.email).isEqualTo(member.email)
        assertThat(foundMember?.password).isEqualTo(member.password)
        assertThat(foundMember?.role).isEqualTo(member.role)
    }

    @Test
    fun `it should return null when member not found by id`() {
        val nonExistentId = UUID.randomUUID()
        val foundMember = memberRepository.findById(nonExistentId)
        assertThat(foundMember).isNull()
    }

    @Test
    fun `it should return null when member not found by email`() {
        val nonExistentEmail = "nonexistent@email.com"
        val foundMember = memberRepository.findByEmail(nonExistentEmail)
        assertThat(foundMember).isNull()
    }
}
