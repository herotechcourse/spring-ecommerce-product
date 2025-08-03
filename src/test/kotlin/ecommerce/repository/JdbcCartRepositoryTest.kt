package ecommerce.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
@Import(JdbcCartRepository::class) // Включаем наш репозиторий в контекст
class JdbcCartRepositoryTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var cartRepository: CartRepository

    private val memberId = 1L
    private val productId = 100L

    @BeforeEach
    fun setup() {
        jdbcTemplate.update("DELETE FROM cart_items")
        jdbcTemplate.update("DELETE FROM products")
        jdbcTemplate.update("DELETE FROM members")

        jdbcTemplate.update(
            "INSERT INTO members (id, email, password) VALUES (?, ?, ?)",
            1L,
            "test@example.com",
            "secret",
        )

        jdbcTemplate.update(
            "INSERT INTO products (id, name, price, image_url) VALUES (?, ?, ?, ?)",
            100L,
            "Test Product",
            9.99,
            "http://img",
        )

        jdbcTemplate.update(
            "INSERT INTO cart_items (member_id, product_id, quantity) VALUES (?, ?, ?)",
            1L,
            100L,
            2,
        )

        jdbcTemplate.update(
            "INSERT INTO products (id, name, price, image_url) VALUES (?, ?, ?, ?)",
            200L,
            "Sample Product",
            9.99,
            "http://example.com/image.jpg",
        )
    }

    @Test
    fun `findByMemberId should return items for given member`() {
        val items = cartRepository.findByMemberId(memberId)

        Assertions.assertThat(items).hasSize(1)
        Assertions.assertThat(items[0].productId).isEqualTo(productId)
        Assertions.assertThat(items[0].quantity).isEqualTo(2)
    }

    @Test
    fun `insert should add new cart item`() {
        val newProductId = 200L
        cartRepository.insert(memberId, newProductId, 3)

        val item = cartRepository.findByMemberIdAndProductId(memberId, newProductId)

        Assertions.assertThat(item).isNotNull
        Assertions.assertThat(item!!.quantity).isEqualTo(3)
    }

    @Test
    fun `updateQuantity should update existing item`() {
        cartRepository.updateQuantity(memberId, productId, 5)

        val item = cartRepository.findByMemberIdAndProductId(memberId, productId)

        Assertions.assertThat(item).isNotNull
        Assertions.assertThat(item!!.quantity).isEqualTo(5)
    }

    @Test
    fun `delete should remove item`() {
        cartRepository.delete(memberId, productId)

        val item = cartRepository.findByMemberIdAndProductId(memberId, productId)
        Assertions.assertThat(item).isNull()
    }

    @Test
    fun `findByMemberIdAndProductId should return correct item`() {
        val item = cartRepository.findByMemberIdAndProductId(memberId, productId)

        Assertions.assertThat(item).isNotNull
        Assertions.assertThat(item!!.quantity).isEqualTo(2)
    }
}
