package ecommerce.service

import ecommerce.dto.CartRequest
import ecommerce.entity.CartItem
import ecommerce.repository.CartRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CartServiceTest(
    @Autowired private val cartService: CartService,
    @Autowired private val cartRepository: CartRepository,
) {
    @Test
    fun `should insert and find cart items as List`() {
        val memberId = 1L
        val request = CartRequest(productId = 100, quantity = 2)
        cartRepository.insert(memberId, request)

        val results = cartService.findByMemberId(memberId)
        assertThat(results).hasSize(1)
    }

    @Test
    fun `if not found cart items return empty list`() {
        val memberId = 1L
        val results = cartService.findByMemberId(memberId)

        assertThat(results).isEqualTo(emptyList<CartItem>())
    }
}
