package ecommerce.cart.repository

import ecommerce.cart.domain.CartItem
import jakarta.validation.ValidationException
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@JdbcTest
@Sql("/schema.sql")
class CartRepositoryTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var cartRepository: CartRepository

    private var memberId: Long = 0
    private var productId: Long = 0

    @BeforeEach
    fun setUp() {
        cartRepository = CartRepository(jdbcTemplate)

        jdbcTemplate.execute("DELETE FROM CART_ITEMS")
        jdbcTemplate.execute("DELETE FROM PRODUCTS")
        jdbcTemplate.execute("DELETE FROM MEMBERS")

        jdbcTemplate.update(
            "INSERT INTO MEMBERS(email, password, role) VALUES (?, ?, ?)",
            "test@example.com",
            "securepassword",
            "USER",
        )

        jdbcTemplate.update(
            "INSERT INTO PRODUCTS(name, price, image_url) VALUES (?, ?, ?)",
            "Cola",
            2.99,
            "https://example.com/cola.jpg",
        )

        memberId =
            jdbcTemplate.queryForObject(
                "SELECT id FROM MEMBERS WHERE email = ?",
                Long::class.java,
                "test@example.com",
            )!!

        productId =
            jdbcTemplate.queryForObject(
                "SELECT id FROM PRODUCTS WHERE name = ?",
                Long::class.java,
                "Cola",
            )!!
    }

    @Test
    fun `insert should save cart item and set id`() {
        val cartItem = CartItem(null, memberId, productId)

        cartRepository.insert(cartItem)

        assertNotNull(cartItem.id)

        val items = cartRepository.findByMemberId(memberId)
        assertEquals(1, items.size)
        assertEquals(productId, items[0].productId)
    }

    @Test
    fun `insert should throw ValidationException if item already exists`() {
        val cartItem = CartItem(null, memberId, productId)
        cartRepository.insert(cartItem)

        val duplicateItem = CartItem(null, memberId, productId)
        assertFailsWith<ValidationException> {
            cartRepository.insert(duplicateItem)
        }
    }

    @Test
    fun `findByMemberId should return empty list if nothing is found`() {
        val items = cartRepository.findByMemberId(999L) // несуществующий memberId
        assertTrue(items.isEmpty())
    }

    @Test
    fun `deleteByMemberIdAndProductId should remove item`() {
        val cartItem = CartItem(null, memberId, productId)
        cartRepository.insert(cartItem)

        cartRepository.deleteByMemberIdAndProductId(memberId, productId)

        val items = cartRepository.findByMemberId(memberId)
        assertTrue(items.isEmpty())
    }

    @Test
    fun `existsByMemberIdAndProductId should return true if item exists`() {
        val cartItem = CartItem(null, memberId, productId)
        cartRepository.insert(cartItem)

        val exists = cartRepository.existsByMemberIdAndProductId(memberId, productId)
        assertTrue(exists)
    }

    @Test
    fun `existsByMemberIdAndProductId should return false if item does not exist`() {
        val exists = cartRepository.existsByMemberIdAndProductId(memberId, 999L)
        assertEquals(false, exists)
    }
}
