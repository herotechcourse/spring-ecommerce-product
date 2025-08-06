package ecommerce.repository

import ecommerce.dto.CartRequest
import ecommerce.dto.mapper.CartMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class CartRepositoryTest {
    private lateinit var cartRepository: CartRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        cartRepository = CartRepository(jdbcTemplate)

        jdbcTemplate.execute("DELETE FROM cart_items")
        jdbcTemplate.execute("DELETE FROM products")
        jdbcTemplate.execute("DELETE FROM members")

        jdbcTemplate.update(
            "INSERT INTO members (id, email, password) VALUES (?, ?, ?)",
            1L,
            "test@example.com",
            "guri_cute_dog",
        )
    }

    @Test
    fun `should insert cart item`() {
        val request = CartRequest(productId = 100, quantity = 2)
        val memberId = 1L
        val insertedId = cartRepository.insertNewItem(CartMapper.toNewItem(memberId, request))

        assertThat(insertedId).isNotNull()
    }

    @Test
    fun `should return true if member has cart items`() {
        val request = CartRequest(productId = 100, quantity = 2)
        val memberId = 1L
        cartRepository.insertNewItem(CartMapper.toNewItem(memberId, request))

        val exists = cartRepository.existsByMemberId(memberId)

        assertThat(exists).isTrue()
    }

    @Test
    fun `should retrieve cart items by member id`() {
        val request = CartRequest(productId = 100, quantity = 2)
        val memberId = 1L
        val insertedId = cartRepository.insertNewItem(CartMapper.toNewItem(memberId, request))

        val results = cartRepository.selectByMemberId(memberId)

        assertThat(results).hasSize(1)
        assertThat(results[0].id).isEqualTo(insertedId)
        assertThat(results[0].productId).isEqualTo(100)
        assertThat(results[0].quantity).isEqualTo(2)
    }
}
