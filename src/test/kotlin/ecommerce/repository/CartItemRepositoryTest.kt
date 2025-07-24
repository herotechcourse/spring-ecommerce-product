package ecommerce.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class CartItemRepositoryTest {
    private lateinit var cartItemRepository: CartItemRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        cartItemRepository = CartItemRepository(jdbcTemplate)
        jdbcTemplate.execute("INSERT INTO carts (id, member_id) VALUES (1, 1)")
    }

    @Test
    fun addProductToCart() {
        cartItemRepository.addProductToCart(1, 1, 3)
        val count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_items", Int::class.java)
        assert(count == 1)
    }
}
