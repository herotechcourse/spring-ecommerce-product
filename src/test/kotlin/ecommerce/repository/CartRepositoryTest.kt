package ecommerce.repository

import ecommerce.dto.CartRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CartRepositoryTest
    @Autowired constructor(
        private val cartRepository: CartRepository,
) {
    @Test
    fun `should insert cart item`() {
        val request = CartRequest(productId = 100, quantity = 2)
        val memberId = 1L

        val insertedId = cartRepository.insert(memberId, request)

        assertThat(insertedId).isNotNull()
    }

    @Test
    fun `should return true if member has cart items`() {
        val request = CartRequest(productId = 100, quantity = 2)
        val memberId = 1L
        cartRepository.insert(memberId, request)

        val exists = cartRepository.existsByMemberId(memberId)

        assertThat(exists).isTrue()
    }

    @Test
    fun `should retrieve cart items by member id`() {
        val request = CartRequest(productId = 100, quantity = 2)
        val memberId = 1L
        val insertedId = cartRepository.insert(memberId, request)

        val results = cartRepository.selectByMemberId(memberId)

        assertThat(results).hasSize(1)
        assertThat(results[0].id).isEqualTo(insertedId)
        assertThat(results[0].productId).isEqualTo(100)
        assertThat(results[0].quantity).isEqualTo(2)
    }
}
