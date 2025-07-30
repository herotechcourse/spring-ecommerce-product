package ecommerce.controller

import ecommerce.config.LoginMemberArgumentResolver
import ecommerce.dto.CartResponse
import ecommerce.dto.MemberRequest
import ecommerce.entity.CartItem
import ecommerce.entity.Member
import ecommerce.exception.NotFoundException
import ecommerce.service.AuthService
import ecommerce.service.CartService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDateTime

@WebMvcTest(CartController::class)
class CartControllerTest
    @Autowired
    constructor(
        private val mockMvc: MockMvc,
    ) {
        @MockitoBean
        private lateinit var cartService: CartService

        @MockitoBean
        private lateinit var authService: AuthService

        @MockitoBean
        private lateinit var loginMemberArgumentResolver: LoginMemberArgumentResolver

        @BeforeEach
        fun setup() {
            val request = MemberRequest(email = "guri@email.com", password = "very_cute_dog")
            val mockMember = Member(id = 1L, email = request.email, password = request.password)
            whenever(loginMemberArgumentResolver.supportsParameter(any())).thenReturn(true)
            whenever(loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(mockMember)
        }

        @Test
        fun `should return cart items as JSON`() {
            val memberId = 1L
            val mockCartItem =
                CartItem(
                    id = 1L,
                    memberId = memberId,
                    productId = 101,
                    quantity = 2,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                )

            whenever(cartService.findByMemberId(memberId))
                .thenReturn(listOf(mockCartItem))

            mockMvc.get("/api/cart-items") {
                header("Authorization", "Bearer fake-token")
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$[0].productId") { value(101) }
                    jsonPath("$[0].quantity") { value(2) }
                }
        }

        @Test
        fun `should add single item to cart`() {
            val memberId = 1L
            val productId = 101L
            val mockResponse = CartResponse(id = 1L, productId = productId, quantity = 2, updatedAt = LocalDateTime.now())

            whenever(cartService.insertNewItemToCart(memberId, productId, 2))
                .thenReturn(mockResponse)

            mockMvc.post("/api/cart-items/$productId") {
                header("Authorization", "Bearer fake-token")
                contentType = MediaType.APPLICATION_JSON
                content = """{"quantity": 2}"""
            }
                .andExpect {
                    status { isCreated() }
                    jsonPath("$.productId") { value(101) }
                    jsonPath("$.quantity") { value(2) }
                }
        }

        @Test
        fun `should update multiple cart items`() {
            val memberId = 1L
            val mockResponses =
                listOf(
                    CartResponse(id = 1L, productId = 101, quantity = 2, updatedAt = LocalDateTime.now()),
                    CartResponse(id = 2L, productId = 102, quantity = 3, updatedAt = LocalDateTime.now()),
                )

            whenever(cartService.insertNewItemsToCart(eq(memberId), any()))
                .thenReturn(mockResponses)

            mockMvc.put("/api/cart-items/") {
                header("Authorization", "Bearer fake-token")
                contentType = MediaType.APPLICATION_JSON
                content = """
            [
                {"productId": 101, "quantity": 2},
                {"productId": 102, "quantity": 3}
            ]
        """
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$[0].productId") { value(101) }
                    jsonPath("$[1].quantity") { value(3) }
                }
        }

        @Test
        fun `should delete cart item`() {
            val memberId = 1L
            val productId = 101L

            mockMvc.delete("/api/cart-items/$productId") {
                header("Authorization", "Bearer fake-token")
            }
                .andExpect {
                    status { isNoContent() }
                }
        }

        @Test
        fun `should return 404 when deleting non-existing item`() {
            val memberId = 1L
            val productId = 999L

            whenever(cartService.deleteBy(memberId, productId))
                .thenThrow(NotFoundException("not found"))

            mockMvc.delete("/api/cart-items/$productId") {
                header("Authorization", "Bearer fake-token")
            }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
