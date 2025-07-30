package ecommerce.repository

import ecommerce.domain.CartEvent
import ecommerce.domain.Member
import ecommerce.domain.Product
import ecommerce.repository.reportDto.MemberCartActivityDto
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
import java.time.temporal.ChronoUnit

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CartRepository::class, MemberRepository::class, ProductRepository::class, CartEventRepository::class)
@DisplayName("Cart Event Repository Tests")
class CartEventRepositoryTest
    @Autowired
    constructor(
        private val cartEventRepository: CartEventRepository,
        private val memberRepository: MemberRepository,
        private val productRepository: ProductRepository,
        private val jdbcTemplate: JdbcTemplate,
    ) {
        private fun createTestMember(email: String = "bo@gmail.com"): Member {
            val member = Member(userName = "bo", email = email, passwordHash = "MojataBebushkaAngie10%", role = "USER")
            memberRepository.create(member)
            return member
        }

        private fun createTestProduct(
            name: String = "lotion",
            price: Double = 20.0,
            quantity: Int = 10,
        ): Product {
            val product = Product(name = name, price = price, img = "http://img.png", quantity = quantity)
            productRepository.create(product)
            return product
        }

        private fun createTestCartEvent(
            memberId: Long,
            productId: Long,
            quantity: Int,
            timestamp: LocalDateTime = LocalDateTime.now(),
        ): CartEvent {
            val event =
                CartEvent(
                    memberId = memberId,
                    productId = productId,
                    quantityAdded = quantity,
                    timestamp = timestamp,
                )
            return cartEventRepository.save(event)
        }

        @BeforeEach
        fun setUp() {
            jdbcTemplate.execute("delete from cart_items")
            jdbcTemplate.execute("delete from cart_events")
            jdbcTemplate.execute("delete from carts")
            jdbcTemplate.execute("delete from members")
            jdbcTemplate.execute("delete from products")
        }

        @Test
        fun `it should save a cart event`() {
            val member = createTestMember()
            val product = createTestProduct()
            val initialCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_items", Int::class.java) ?: 0

            val savedEvent = createTestCartEvent(member.userId, product.id, 2)

            assertThat(savedEvent.id).isNotNull().isGreaterThan(0)
            assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_events", Int::class.java)).isEqualTo(
                initialCount + 1,
            )
        }

        @Test
        fun `findTop5MostAddedProductsInLast30Days should return an empty list if no events`() {
            val result = cartEventRepository.findTop5MostAddedProductsInLast30Days(LocalDateTime.now().minusDays(30))
            assertThat(result).isEmpty()
        }

        @Test
        fun `findTop5MostAddedProductsInLast30Days should return correct top products`() {
            val member1 = createTestMember(email = "bo@gmail.com")
            val member2 = createTestMember(email = "elena@gmail")

            val product1 = createTestProduct(name = "spf1")
            val product2 = createTestProduct(name = "spf2")
            val product3 = createTestProduct(name = "spf3")
            val product4 = createTestProduct(name = "spf4")
            val product5 = createTestProduct(name = "spf5")

            val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

            repeat(8) { createTestCartEvent(member1.userId, product1.id, quantity = 2, now.minusHours(it.toLong())) }
            repeat(7) { createTestCartEvent(member1.userId, product2.id, quantity = 3, now.minusHours(it.toLong())) }
            repeat(10) { createTestCartEvent(member2.userId, product3.id, quantity = 4, now.minusHours(it.toLong())) }
            repeat(6) { createTestCartEvent(member2.userId, product4.id, quantity = 2, now.minusHours(it.toLong())) }
            repeat(5) { createTestCartEvent(member1.userId, product5.id, quantity = 2, now.minusHours(it.toLong())) }

            val result = cartEventRepository.findTop5MostAddedProductsInLast30Days(LocalDateTime.now().minusDays(30))

            assertThat(result).hasSize(5)
            assertThat(result[0].productName).isEqualTo(product3.name)
            assertThat(result[0].addedCount).isEqualTo(10)
            assertThat(result[1].productName).isEqualTo(product1.name)
            assertThat(result[1].addedCount).isEqualTo(8)
            assertThat(result[2].productName).isEqualTo(product2.name)
            assertThat(result[2].addedCount).isEqualTo(7)
            assertThat(result[3].productName).isEqualTo(product4.name)
            assertThat(result[3].addedCount).isEqualTo(6)
            assertThat(result[4].productName).isEqualTo(product5.name)
            assertThat(result[4].addedCount).isEqualTo(5)
        }

        @Test
        fun `findTop5MostAddedProductsInLast30Days should filter by date`() {
            val member = createTestMember()
            val productOld = createTestProduct(name = "spf1")
            val productNew = createTestProduct(name = "spf2")

            createTestCartEvent(member.userId, productOld.id, 1, LocalDateTime.now().minusDays(31))
            createTestCartEvent(member.userId, productNew.id, 1, LocalDateTime.now().minusDays(20))

            val result = cartEventRepository.findTop5MostAddedProductsInLast30Days(LocalDateTime.now().minusDays(30))

            assertThat(result).hasSize(1)
            assertThat(result[0].productName).isEqualTo(productNew.name)
        }

        @Test
        fun `findTop5MostAddedProductsInLast30Days should handle products with same count, ordered by latest added time`() {
            val member = createTestMember()
            val product1 = createTestProduct(name = "spf1")
            val product2 = createTestProduct(name = "spf2")

            val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

            createTestCartEvent(member.userId, product1.id, 1, now.minusHours(3))
            createTestCartEvent(member.userId, product1.id, 1, now.minusHours(2))
            createTestCartEvent(member.userId, product1.id, 1, now.minusHours(1))

            createTestCartEvent(member.userId, product2.id, 1, now.minusHours(6))
            createTestCartEvent(member.userId, product2.id, 1, now.minusHours(5))
            createTestCartEvent(member.userId, product2.id, 1, now.minusHours(4))

            val result = cartEventRepository.findTop5MostAddedProductsInLast30Days(LocalDateTime.now().minusDays(30))

            assertThat(result).hasSize(2)
            assertThat(result[0].productName).isEqualTo(product1.name)
            assertThat(result[0].addedCount).isEqualTo(3)
            assertThat(result[1].productName).isEqualTo(product2.name)
            assertThat(result[1].addedCount).isEqualTo(3)
        }

        @Test
        fun `findMembersWhoAddedItemsInLastDays should return unique members within 7 days`() {
            val member1 = createTestMember(email = "bo@gmail.com")
            val member2 = createTestMember(email = "elena@gmail.com")
            val member3 = createTestMember(email = "ace@gmail.com")

            val product1 = createTestProduct(name = "spf1")
            val product2 = createTestProduct(name = "spf2")
            val product3 = createTestProduct(name = "spf3")

            val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

            createTestCartEvent(member1.userId, product1.id, 1, now.minusDays(1))
            createTestCartEvent(member1.userId, product2.id, 1, now.minusDays(2))
            createTestCartEvent(member2.userId, product1.id, 1, now.minusDays(5))
            createTestCartEvent(member3.userId, product3.id, 1, now.minusDays(8))

            val expectedMembers =
                listOf(
                    MemberCartActivityDto(userId = member1.userId, userName = member1.userName, email = member1.email),
                    MemberCartActivityDto(userId = member2.userId, userName = member2.userName, email = member2.email),
                ).sortedBy { it.userId }

            val result = cartEventRepository.findMembersWhoAddedItemsInLastDays(now.minusDays(7))

            assertThat(result).hasSize(2)
            assertThat(result.sortedBy { it.userId }).isEqualTo(expectedMembers)
        }
    }
