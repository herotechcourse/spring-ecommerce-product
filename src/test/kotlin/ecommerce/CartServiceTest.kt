package ecommerce.cart.service

import ecommerce.cart.dto.CartRequest
import ecommerce.member.domain.Member
import ecommerce.member.repository.MemberRepository
import ecommerce.product.domain.Product
import ecommerce.product.repository.ProductRepository
import jakarta.validation.ValidationException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest
class CartServiceTest {
    @Autowired lateinit var cartService: CartService

    @Autowired lateinit var memberRepository: MemberRepository

    @Autowired lateinit var productRepository: ProductRepository

    @Autowired lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var member: Member
    private lateinit var product: Product

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM CART_ITEMS")
        jdbcTemplate.execute("DELETE FROM PRODUCTS")
        jdbcTemplate.execute("DELETE FROM MEMBERS")

        member =
            Member(null, "ann@gmail.com", "password123qwerty", "USER").also {
                memberRepository.insert(it)
            }

        product =
            Product(null, "TestProduct", 19.99, "https://image").also {
                productRepository.insert(it)
            }
    }

    @Test
    fun `addToCart should add product successfully`() {
        val response = cartService.addToCart(member, CartRequest(productId = product.id))

        assertThat(response.id).isNotNull()
        assertThat(response.product.name).isEqualTo("TestProduct")
    }

    @Test
    fun `getCartItems should return all items`() {
        cartService.addToCart(member, CartRequest(productId = product.id))
        val items = cartService.getCartItems(member)

        assertThat(items).hasSize(1)
        assertThat(items[0].product.id).isEqualTo(product.id)
    }

    @Test
    fun `removeFromCart should remove item successfully`() {
        cartService.addToCart(member, CartRequest(productId = product.id))
        cartService.removeFromCart(member, product.id!!)

        val items = cartService.getCartItems(member)
        assertThat(items).isEmpty()
    }

    @Test
    fun `addToCart should throw for duplicate product`() {
        cartService.addToCart(member, CartRequest(productId = product.id))

        assertThatThrownBy {
            cartService.addToCart(member, CartRequest(productId = product.id))
        }.isInstanceOf(ValidationException::class.java)
            .hasMessage("Product is already in the cart")
    }

    @Test
    fun `removeFromCart should throw if product not in cart`() {
        assertThatThrownBy {
            cartService.removeFromCart(member, product.id!!)
        }.isInstanceOf(ValidationException::class.java)
            .hasMessage("Product not found in cart")
    }
}
