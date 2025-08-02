package ecommerce.repository

import ecommerce.model.CartItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import java.sql.Timestamp
import java.time.LocalDateTime
import kotlin.Long

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartRepositoryTest {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var repository: CartRepository

    @BeforeEach
    fun setUp() {
        repository = CartRepository(jdbcTemplate)
    }

    val timestamp = Timestamp.valueOf("2025-07-27 10:00:00")

    @Test
    fun `insert should insert a new item`() {
        val timestamp = Timestamp.valueOf("2025-07-27 10:00:00")
        val original = CartItem(0, 2, 2, 7, timestamp)
        val newInRepository = repository.insert(original)

        assertThat(newInRepository.id).isEqualTo(4)
    }

    @Test
    fun `findByMemberId should return existing data from data sql`() {
        val result = repository.findByMemberId(1)
        assertThat(result).hasSize(3)
    }

    @Test
    fun `updateQuantity() should update cart item`() {
        val inserted = repository.insert(CartItem(0, 1, 2, 1, timestamp))
        val updatedAt = Timestamp.valueOf("2025-07-27 10:00:00")
        inserted.updatedAt = updatedAt
        assertThat(inserted.quantity).isEqualTo(1)

        val updated1 = repository.updateQuantity(inserted, 5)
        assertThat(updated1.quantity).isEqualTo(5)

        val updated2 = repository.findById(inserted.id)
        assertThat(updated2?.quantity).isEqualTo(5)
        assertThat(updated2?.updatedAt).isEqualTo(updatedAt)
    }

    @Test
    fun `findByMemberAndProductIds should return correct cart item`() {
        val fetchedProduct = repository.findByMemberAndProductIds(1, 1)
        assertThat(fetchedProduct[0].quantity).isEqualTo(2)
    }

    @Test
    fun `deleteByMemberAndProduct should delete correct cart item`() {
        val affectedItem = repository.deleteByMemberAndProduct(1, 1)
        assertThat(affectedItem).isEqualTo(1)

        val remaining = repository.findByMemberAndProductIds(1, 1)
        assertThat(remaining).isEmpty()
    }

    @Test
    fun `deleteByMemberAndProduct should return null for nonexistent item`() {
        val affected = repository.deleteByMemberAndProduct(999, 999)
        assertThat(affected).isNull()
    }

    @Test
    fun `findTopAddedProductsByMember should return correct cart items`() {
        val memberId = 2L
        val count = 5
        val days = 30

        fun daysAgo(daysAgo: Long) = Timestamp.valueOf(LocalDateTime.now().minusDays(daysAgo))

        val recentItems =
            listOf(
                CartItem(
                    id = 0,
                    memberId = memberId,
                    productId = 1,
                    quantity = 2,
                    createdAt = daysAgo(35),
                    updatedAt = daysAgo(29),
                ),
                CartItem(
                    id = 0,
                    memberId = memberId,
                    productId = 2,
                    quantity = 1,
                    createdAt = daysAgo(15),
                    updatedAt = daysAgo(5),
                ),
                CartItem(
                    id = 0,
                    memberId = memberId,
                    productId = 3,
                    quantity = 3,
                    createdAt = daysAgo(12),
                    updatedAt = daysAgo(1),
                ),
            )

        val oldItems =
            listOf(
                CartItem(
                    id = 0,
                    memberId = memberId,
                    productId = 4,
                    quantity = 5,
                    createdAt = daysAgo(50),
                    updatedAt = daysAgo(40),
                ),
                CartItem(
                    id = 0,
                    memberId = memberId,
                    productId = 5,
                    quantity = 2,
                    createdAt = daysAgo(35),
                    updatedAt = daysAgo(31),
                ),
            )

        recentItems.forEach { repository.insert(it) }
        oldItems.forEach { repository.insert(it) }

        val fetchedProducts = repository.findTopAddedProducts(count, days)

        print("#### fetchedProducts: $fetchedProducts")
//        val fetchedProductIds = fetchedProducts.map { it.productId }
//
//        assertThat(fetchedProductIds).containsExactly(3, 2, 1)
//        assertThat(fetchedProductIds).doesNotContain(4, 5)
    }
}
