package ecommerce.controller

import ecommerce.dto.MemberRequest
import ecommerce.dto.MemberStatsResponse
import ecommerce.dto.ProductStatsResponse
import ecommerce.entity.Member
import ecommerce.helper.MemberTestFixture.Cases.VALID_REQUEST_GURI
import ecommerce.service.AdminService
import ecommerce.service.AuthService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime

@WebMvcTest(AdminController::class)
class AdminControllerTest
    @Autowired
    constructor(private val mockMvc: MockMvc) {
        @MockitoBean
        private lateinit var adminService: AdminService

        @MockitoBean
        private lateinit var authService: AuthService

        @BeforeEach
        fun setup() {
            val request = MemberRequest(email = "guri@email.com", password = "very_cute_dog")
            val mockMember = Member(id = 1L, email = request.email, password = request.password)
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
        fun `should return top products when products exist`() {
            val mockProducts =
                listOf(
                    ProductStatsResponse(
                        productId = 1L,
                        addCount = 10L,
                        lastAdded = LocalDateTime.now(),
                    ),
                    ProductStatsResponse(
                        productId = 2L,
                        addCount = 8L,
                        lastAdded = LocalDateTime.now(),
                    ),
                )

            given(adminService.getTopProducts(AdminController.TOP_5_LAST_30_DAYS))
                .willReturn(mockProducts)

            mockMvc.get("/api/admin/stats/products/top") {
                accept = MediaType.APPLICATION_JSON
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.size()") { value(2) }
                    jsonPath("$[0].productId") { value(1) }
                    jsonPath("$[0].addCount") { value(10) }
                    jsonPath("$[1].productId") { value(2) }
                }
        }

        @Test
        fun `should return ok status with empty list when no products found`() {
            val mockProducts = emptyList<ProductStatsResponse>()

            given(adminService.getTopProducts(AdminController.TOP_5_LAST_30_DAYS))
                .willReturn(mockProducts)

            mockMvc.get("/api/admin/stats/products/top") {
                accept = MediaType.APPLICATION_JSON
            }
                .andExpect {
                    status { isOk() }
                }
        }

        @Test
        fun `should return active members who added products in last 7 days`() {
            val member = VALID_REQUEST_GURI
            val mockProducts =
                listOf(
                    MemberStatsResponse(
                        memberId = 1L,
                        email = member.email,
                    ),
                )

            given(adminService.getActiveMembers(AdminController.ACTIVE_MEMBERS_LAST_7_DAYS))
                .willReturn(mockProducts)

            mockMvc.get("/api/admin/stats/members/active") {
                accept = MediaType.APPLICATION_JSON
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.size()") { value(1) }
                    jsonPath("$[0].memberId") { value(1L) }
                    jsonPath("$[0].email") { value(member.email) }
                }
        }
    }
