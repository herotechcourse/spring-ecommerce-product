package ecommerce.repository

import ecommerce.domain.Cart
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
import java.time.LocalDateTime

@JdbcTest
@Import(CartRepository::class, MemberRepository::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Cart Repository Tests")
class CartRepositoryTest
    @Autowired
    constructor(
        private val cartRepository: CartRepository,
        private val jdbcTemplate: JdbcTemplate,
        private val memberRepository: MemberRepository,
    ) {
        private fun createTestMember(email: String = "bo@gmail.com"): Member {
            val member = Member(userName = "bo", email = email, passwordHash = "MojataBebushkaAngie10%", role = "USER")
            memberRepository.create(member)
            return member
        }

        @BeforeEach
        fun setUp() {
            jdbcTemplate.execute("delete from cart_items")
            jdbcTemplate.execute("delete from carts")
            jdbcTemplate.execute("delete from members")
        }

        @Test
        fun `it should create a new cart for a member`() {
            val member = createTestMember()

            val initialCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM carts", Int::class.java) ?: 0

            val newCart =
                Cart(
                    memberId = member.userId,
                    createdAt = LocalDateTime.now().withNano(0),
                )
            cartRepository.create(newCart)

            assertThat(newCart.id).isGreaterThan(0)
            assertThat(
                jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM carts",
                    Int::class.java,
                ),
            ).isEqualTo(initialCount + 1)

            val foundCart = cartRepository.findByMemberId(member.userId)
            assertThat(foundCart).isNotNull()
            assertThat(foundCart?.id).isEqualTo(newCart.id)
            assertThat(foundCart?.memberId).isEqualTo(newCart.memberId)
            assertThat(foundCart?.createdAt).isEqualTo(newCart.createdAt)
        }

        @Test
        fun `it should find an existing cart by member ID`() {
            val member = createTestMember("bo@gmail.com")

            val expectedCart =
                Cart(
                    memberId = member.userId,
                    createdAt = LocalDateTime.now().withNano(0),
                )
            cartRepository.create(expectedCart)

            val foundCart = cartRepository.findByMemberId(member.userId)

            assertThat(foundCart).isNotNull()
            assertThat(foundCart).isEqualTo(expectedCart)
        }

        @Test
        fun `it should return null when finding a cart for a non-existent member`() {
            val nonExistentMemberId = 12345L

            val foundCart = cartRepository.findByMemberId(nonExistentMemberId)

            assertThat(foundCart).isNull()
        }
    }
