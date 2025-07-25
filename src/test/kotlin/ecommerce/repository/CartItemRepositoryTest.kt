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
        jdbcTemplate.execute("DELETE FROM cart_history")
        jdbcTemplate.execute("DELETE FROM cart_items")
        jdbcTemplate.execute("DELETE FROM carts")
        jdbcTemplate.execute("DELETE FROM products")

        jdbcTemplate.execute("INSERT INTO products (id, name, price, image_url) VALUES (1, 'Test Product', 9.99, 'url')")
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
        val id = cartItemRepository.addProductToCart(1, 1, 3)
        cartItemRepository.deleteProductFromCart(id)
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
        val quantity =
            jdbcTemplate.queryForObject(
                "SELECT quantity FROM cart_items WHERE cart_id = ? AND product_id = ?",
                Int::class.java,
                1,
                1,
            )
        assert(quantity == 5)
    }
}
