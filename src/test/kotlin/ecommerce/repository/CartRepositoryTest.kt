package ecommerce.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
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
        jdbcTemplate.execute("DELETE FROM carts")
        jdbcTemplate.execute("DELETE FROM products")
    }

    @Test
    fun createCart() {
        val memberId = 1L
        val cartId = cartRepository.createCart(memberId)
        assertTrue(cartId > 0)
        val count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM carts WHERE member_id = $memberId", Int::class.java)
        assertEquals(1, count)
    }

    @Test
    fun findCartByMemberId() {
        val memberId = 1L
        cartRepository.createCart(memberId)
        val cart = cartRepository.findCartByMemberId(memberId)
        assertNotNull(cart)
    }
}
