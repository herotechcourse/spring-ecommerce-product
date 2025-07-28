package ecommerce.service

import ecommerce.dto.CartRequest
import ecommerce.entity.CartItem
import ecommerce.repository.CartRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Rollback
class CartServiceTest(
    @Autowired private val cartService: CartService,
    @Autowired private val cartRepository: CartRepository,

) {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun clearTable() {
        jdbcTemplate.execute("TRUNCATE TABLE cart_items")
    }

    @Test
    fun `should insert and find cart items as List`() {
        val memberId = 1L
        val request = CartRequest(productId = 100, quantity = 2)
        cartRepository.insert(memberId, request)

        val results = cartService.findByMemberId(memberId)
        assertThat(results).hasSize(1)
    }

    @Test
    fun `if cart items not found return empty list`() {
        val memberId = 2L
        val results = cartService.findByMemberId(memberId)

        assertThat(results).isEqualTo(emptyList<CartItem>())
    }
}
