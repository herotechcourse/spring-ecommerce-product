package ecommerce.controller

import com.ninjasquad.springmockk.MockkBean
import ecommerce.auth.AdminOnlyInterceptor
import ecommerce.auth.LoginMemberArgumentResolver
import ecommerce.config.WebConfig
import ecommerce.dto.ActiveMemberStat
import ecommerce.dto.TopProductStat
import ecommerce.model.Member
import ecommerce.repository.CartStatRepository
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime

@WebMvcTest(
    StatsController::class,
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [WebConfig::class],
        ),
    ],
)
class StatsControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var cartStatRepository: CartStatRepository

    @MockkBean
    private lateinit var adminOnlyInterceptor: AdminOnlyInterceptor

    @MockkBean
    private lateinit var loginMemberArgumentResolver: LoginMemberArgumentResolver

    @Test
    fun `should return top 5 most added products`() {
        val mockResponse =
            listOf(
                TopProductStat(1L, "Apple", 5, LocalDateTime.of(2025, 7, 28, 13, 0)),
                TopProductStat(2L, "Banana", 4, LocalDateTime.of(2025, 7, 28, 13, 0)),
            )
        every { cartStatRepository.findTop5MostAddedProductsInLast30Days() } returns mockResponse
        every { adminOnlyInterceptor.preHandle(any(), any(), any()) } returns true
        every { loginMemberArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns Member(email = "a@a", password = "123")

        mockMvc.get("/stats/top-products") {
            header("Authorization", "Bearer admin-token")
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.size()") { value(2) }
                jsonPath("$[0].productName") { value("Apple") }
            }
    }

    @Test
    fun `should return active members`() {
        val mockResponse =
            listOf(
                ActiveMemberStat(1L, "", "user1@email.com"),
                ActiveMemberStat(2L, "", "user2@email.com"),
            )
        every { cartStatRepository.findActiveMembersInLast7Days() } returns mockResponse

        mockMvc.get("/stats/active-members") {
            header("Authorization", "Bearer admin-token")
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.size()") { value(2) }
                jsonPath("$[0].email") { value("user1@email.com") }
            }
    }
}
