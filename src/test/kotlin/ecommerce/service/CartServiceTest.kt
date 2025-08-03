package ecommerce.service

import ecommerce.model.CartItem
import ecommerce.repository.CartRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class CartServiceTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var cartService: CartService

    @BeforeEach
    fun setUp() {
        cartRepository = mockk()
        cartService = CartService(cartRepository)
    }

    @Test
    fun `getCart should return cart items for member`() {
        val memberId = 1L
        val now = LocalDateTime.now()
        val expectedCartItems =
            listOf(
                CartItem(1L, 101L, 2, now),
                CartItem(1L, 102L, 1, now),
            )
        every { cartRepository.findByMemberId(memberId) } returns expectedCartItems

        val result = cartService.getCart(memberId)

        assertEquals(expectedCartItems, result)
        verify { cartRepository.findByMemberId(memberId) }
    }

    @Test
    fun `getCart should return empty list when no items exist`() {
        val memberId = 1L
        every { cartRepository.findByMemberId(memberId) } returns emptyList()

        val result = cartService.getCart(memberId)

        assertEquals(emptyList(), result)
        verify { cartRepository.findByMemberId(memberId) }
    }

    @Test
    fun `addProduct should insert new product when not exists in cart`() {
        val memberId = 1L
        val productId = 101L
        val quantity = 3
        every { cartRepository.findByMemberIdAndProductId(memberId, productId) } returns null
        every { cartRepository.insert(memberId, productId, quantity) } returns Unit

        cartService.addProduct(memberId, productId, quantity)

        verify { cartRepository.findByMemberIdAndProductId(memberId, productId) }
        verify { cartRepository.insert(memberId, productId, quantity) }
    }

    @Test
    fun `addProduct should update quantity when product already exists in cart`() {
        val memberId = 1L
        val productId = 101L
        val existingQuantity = 2
        val additionalQuantity = 3
        val expectedTotalQuantity = existingQuantity + additionalQuantity
        val now = LocalDateTime.now()

        val existingCartItem = CartItem(memberId, productId, existingQuantity, now)
        every { cartRepository.findByMemberIdAndProductId(memberId, productId) } returns existingCartItem
        every { cartRepository.updateQuantity(memberId, productId, expectedTotalQuantity) } returns Unit

        cartService.addProduct(memberId, productId, additionalQuantity)

        verify { cartRepository.findByMemberIdAndProductId(memberId, productId) }
        verify { cartRepository.updateQuantity(memberId, productId, expectedTotalQuantity) }
    }

    @Test
    fun `addProduct should handle zero quantity correctly for new product`() {
        val memberId = 1L
        val productId = 101L
        val quantity = 0
        every { cartRepository.findByMemberIdAndProductId(memberId, productId) } returns null
        every { cartRepository.insert(memberId, productId, quantity) } returns Unit

        cartService.addProduct(memberId, productId, quantity)

        verify { cartRepository.findByMemberIdAndProductId(memberId, productId) }
        verify { cartRepository.insert(memberId, productId, quantity) }
    }

    @Test
    fun `addProduct should handle zero quantity correctly for existing product`() {
        val memberId = 1L
        val productId = 101L
        val existingQuantity = 5
        val additionalQuantity = 0
        val expectedTotalQuantity = existingQuantity + additionalQuantity
        val now = LocalDateTime.now()

        val existingCartItem = CartItem(memberId, productId, existingQuantity, now)
        every { cartRepository.findByMemberIdAndProductId(memberId, productId) } returns existingCartItem
        every { cartRepository.updateQuantity(memberId, productId, expectedTotalQuantity) } returns Unit

        cartService.addProduct(memberId, productId, additionalQuantity)

        verify { cartRepository.findByMemberIdAndProductId(memberId, productId) }
        verify { cartRepository.updateQuantity(memberId, productId, expectedTotalQuantity) }
    }

    @Test
    fun `addProduct should handle negative quantity for new product`() {
        val memberId = 1L
        val productId = 101L
        val quantity = -2
        every { cartRepository.findByMemberIdAndProductId(memberId, productId) } returns null
        every { cartRepository.insert(memberId, productId, quantity) } returns Unit

        cartService.addProduct(memberId, productId, quantity)

        verify { cartRepository.findByMemberIdAndProductId(memberId, productId) }
        verify { cartRepository.insert(memberId, productId, quantity) }
    }

    @Test
    fun `removeProduct should delete product from cart`() {
        val memberId = 1L
        val productId = 101L
        every { cartRepository.delete(memberId, productId) } returns Unit

        cartService.removeProduct(memberId, productId)

        verify { cartRepository.delete(memberId, productId) }
    }

    @Test
    fun `removeProduct should handle non-existing product gracefully`() {
        val memberId = 1L
        val productId = 999L
        every { cartRepository.delete(memberId, productId) } returns Unit

        cartService.removeProduct(memberId, productId)

        verify { cartRepository.delete(memberId, productId) }
    }
}
