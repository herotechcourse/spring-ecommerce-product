package ecommerce.dao

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = ["/sql/member.sql", "/sql/product.sql", "/sql/cart_item.sql", "/sql/cart_item_event.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
)
class JdbcCartDaoTest(
    @Autowired private val jdbcCartDao: JdbcCartDao,
) {
    @Test
    fun addItemToCart() {
        val itemId = jdbcCartDao.addItemToCart(MEMBER_ID, PRODUCT_ID)
        assertThat(itemId).isNotNull()
        val cartItems = jdbcCartDao.getCartItemsByMemberId(MEMBER_ID)
        assertThat(cartItems.first().quantity).isEqualTo(1)
    }

    @Test
    fun `addItemToCart() - update quantity when the product already exists in the cart`() {
        addItemToCart()
        val itemId = jdbcCartDao.addItemToCart(MEMBER_ID, PRODUCT_ID)
        assertThat(itemId).isNotNull()
        val cartItems = jdbcCartDao.getCartItemsByMemberId(MEMBER_ID)
        assertThat(cartItems.first().quantity).isEqualTo(2)
    }

    @Test
    fun getCartItemsByMemberId() {
        addItemToCart()
        val cartItems = jdbcCartDao.getCartItemsByMemberId(MEMBER_ID)
        assertThat(cartItems).hasSize(1)
    }

    @Test
    fun removeItemFromCart() {
        addItemToCart()
        assertThat(jdbcCartDao.removeItemFromCart(MEMBER_ID, PRODUCT_ID)).isEqualTo(1)
        assertThat(jdbcCartDao.getCartItemsByMemberId(MEMBER_ID)).hasSize(0)
    }

    @Test
    fun updateItemQuantityInCart() {
        addItemToCart()
        assertThat(jdbcCartDao.updateItemQuantityInCart(MEMBER_ID, PRODUCT_ID, QUANTITY)).isEqualTo(1)
        val cartItems = jdbcCartDao.getCartItemsByMemberId(MEMBER_ID)
        assertThat(cartItems.first().quantity).isEqualTo(QUANTITY)
    }

    @Test
    fun getTop5AddedProductsInLast30Days() {
        val stats = jdbcCartDao.getTop5AddedProductsInLast30Days()
        assertThat(stats).hasSize(5)
    }

    @Test
    fun getActiveMembersInLast7Days() {
        val stats = jdbcCartDao.getActiveMembersInLast7Days()
        assertThat(stats).hasSize(3)
    }

    companion object {
        const val MEMBER_ID = 1L
        const val PRODUCT_ID = 1L
        const val QUANTITY = 10
    }
}
