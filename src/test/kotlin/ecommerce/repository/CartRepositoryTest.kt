package ecommerce.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
    }

    @Test
    fun createCart() {
        val memberId = 1L
        val cartId = cartRepository.createCart(memberId)
        assertTrue(cartId > 0)
        val count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart WHERE member_id = $memberId", Int::class.java)
        assertEquals(1, count)
    }
}
