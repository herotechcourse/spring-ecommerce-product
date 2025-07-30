package ecommerce.repository

import ecommerce.domain.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
@Import(MemberRepository::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Member Repository Tests")
class MemberRepositoryTest
    @Autowired
    constructor(
        private val memberRepository: MemberRepository,
        private val jdbcTemplate: JdbcTemplate,
    ) {
        @BeforeEach
        fun setUp() {
            jdbcTemplate.execute("delete from members")
        }

        @Test
        fun `it should find a member by id`() {
            val createdMember =
                memberRepository.create(
                    Member(userName = "bo", email = "bo@gmail.com", passwordHash = "MojataBebushkaAngie10%", role = "USER"),
                )
            assertThat(memberRepository.findByMemberId(createdMember.userId)).isNotNull()
            assertThat(memberRepository.findByMemberId(createdMember.userId)).isEqualTo(createdMember)
        }

        @Test
        fun `it should find a member by email`() {
            val createdMember =
                memberRepository.create(
                    Member(userName = "bo", email = "bo@gmail.com", passwordHash = "MojataBebushkaAngie10%", role = "USER"),
                )
            assertThat(memberRepository.findByEmail(createdMember.email)).isEqualTo(createdMember)
        }

        @Test
        fun `it should create a member`() {
            val createdMember =
                memberRepository.create(
                    Member(userName = "bo", email = "bo@gmail.com", passwordHash = "MojataBebushkaAngie10%", role = "USER"),
                )
            assertThat(createdMember).isNotNull()
        }

        @Test
        fun `it should update a member`() {
            val createdMember =
                memberRepository.create(
                    Member(userName = "bo", email = "bo@gmail.com", passwordHash = "MojataBebushkaAngie10%", role = "USER"),
                )
            val updatedMember =
                createdMember.copy(
                    userName = "Bojana",
                    email = "bo@gmail.com",
                    passwordHash = "MojataBebushkaAngie10%",
                    role = "USER",
                )
            memberRepository.update(createdMember.userId, updatedMember)
            assertThat(memberRepository.findByMemberId(createdMember.userId)).isEqualTo(updatedMember)
            assertThat(memberRepository.findByMemberId(createdMember.userId)?.userName).isEqualTo(updatedMember.userName)
        }

        @Test
        fun `it should delete a member by id`() {
            val createdMember =
                memberRepository.create(
                    Member(userName = "bo", email = "bo@gmail.com", passwordHash = "MojataBebushkaAngie10%", role = "USER"),
                )
            memberRepository.delete(createdMember.userId)
            assertThat(memberRepository.findByMemberId(createdMember.userId)).isNull()
        }
    }
