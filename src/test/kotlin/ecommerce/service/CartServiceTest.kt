package ecommerce.service

import ecommerce.exception.NotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = ["/sql/member.sql", "/sql/product.sql", "/sql/cart_item.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
)
class CartServiceTest(
    @Autowired private val cartService: CartService,
) {
    @Test
    fun `addToCart() - should throw exception when productId does not exist`() {
        assertThrows<NotFoundException> { cartService.addToCart(MEMBER_ID, INVALID_PRODUCT_ID, 1) }
    }

    companion object {
        const val MEMBER_ID = 1L
        const val INVALID_PRODUCT_ID = 100L
    }
}
