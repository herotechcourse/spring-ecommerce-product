package ecommerce.controller

import ecommerce.entity.CartItem
import ecommerce.entity.Member
import ecommerce.helper.MemberTestFixture
import ecommerce.service.AuthService
import ecommerce.service.CartService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
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

        @BeforeEach
        fun setup() {
            val mockMember = MemberTestFixture.RequestCases.VALID_MEMBER
            whenever(authService.findMemberByToken("fake-token"))
                .thenReturn(
                    Member(
                        id = 1L,
                        email = mockMember.email,
                        password = mockMember.password,
                    ),
                )
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

            mockMvc.get("/api/cart") {
                header("Authorization", "Bearer fake-token")
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$[0].productId") { value(101) }
                    jsonPath("$[0].quantity") { value(2) }
                }
        }
    }
