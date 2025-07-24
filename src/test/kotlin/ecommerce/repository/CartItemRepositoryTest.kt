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

    @Test
    fun deleteProductFromCart() {
        cartItemRepository.addProductToCart(1, 1, 3)
        cartItemRepository.deleteProductFromCart(1)
        val count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_items", Int::class.java)
        assert(count == 0)
    }

    @Test
    fun getCartItemsByCartId() {
        cartItemRepository.addProductToCart(1, 1, 3)
        val cartItems = cartItemRepository.getCartItemsByCartId(1)
        assert(cartItems.size == 1)
        assert(cartItems[0].productId == 1L)
    }

    @Test
    fun deleteCartItemsByCartIdAndProductId() {
        cartItemRepository.addProductToCart(1, 1, 3)
        cartItemRepository.deleteCartItemsByCartIdAndProductId(1, 1)
        val count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_items", Int::class.java)
        assert(count == 0)
    }

    @Test
    fun updateQuantityByCartIdAndProductId() {
        cartItemRepository.addProductToCart(1, 1, 3)
        cartItemRepository.updateQuantityByCartIdAndProductId(1, 1, 5)
        val quantity = jdbcTemplate.queryForObject("SELECT quantity FROM cart_items WHERE id = 1", Int::class.java)
        assert(quantity == 5)
    }
}
