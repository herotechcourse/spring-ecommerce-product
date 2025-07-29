package ecommerce.controller

import ecommerce.dto.MemberResponse
import ecommerce.dto.TopProductStatResponse
import ecommerce.infrastructure.JWTProvider
import ecommerce.model.UserRole
import ecommerce.service.AuthService
import ecommerce.service.CartService
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.sql.Timestamp

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var cartService: CartService

    @MockitoBean
    private lateinit var jwtProvider: JWTProvider

    @MockitoBean
    private lateinit var authService: AuthService

    private lateinit var topProducts: List<TopProductStatResponse>

    val token = "mocked-jwt-token"

    @BeforeEach
    fun setup() {
        topProducts =
            listOf(
                TopProductStatResponse("Laptop", 15, Timestamp.valueOf("2025-07-27 10:00:00")),
                TopProductStatResponse("Phone", 14, Timestamp.valueOf("2025-07-26 09:30:00")),
                TopProductStatResponse("Tablet", 13, Timestamp.valueOf("2025-07-25 08:00:00")),
                TopProductStatResponse("Monitor", 12, Timestamp.valueOf("2025-07-24 14:15:00")),
                TopProductStatResponse("Keyboard", 11, Timestamp.valueOf("2025-07-23 16:45:00")),
                TopProductStatResponse("Mouse", 10, Timestamp.valueOf("2025-07-22 11:20:00")),
            )
        doNothing().`when`(jwtProvider).validateToken(token)
        `when`(cartService.findTop5ProductsInLast30Days()).thenReturn(topProducts.take(5))
    }

    @Test
    fun `should return top 5 most added products from last 30 days when User role is Admin`() {
        val memberResponse = MemberResponse(1L, "user@example.com", name = "John Doe", role = UserRole.ADMIN.toString())
        `when`(authService.findMemberByToken(token)).thenReturn(memberResponse)

        mockMvc.perform(
            get("/api/protected/admin/top-products")
                .header("Authorization", "Bearer $token"),
        ).andExpect {
            status().isOk
            jsonPath("$.size()").value(5)
            jsonPath("$[0].name").value("Laptop")
            jsonPath("$[0].count").value(15)
            jsonPath("$[0].lastAddedAt").value("2025-07-27T10:00:00")
            jsonPath("$[1].name").value("Phone")
            jsonPath("$[2].name").value("Tablet")
            jsonPath("$[3].name").value("Monitor")
            jsonPath("$[4].name").value("Keyboard")
            jsonPath("$[*].name", not(hasItem("Mouse")))
        }
        verify(cartService).findTop5ProductsInLast30Days()
    }

    @Test
    fun `should return status code 401 when User role is not Admin`() {
        val memberResponse = MemberResponse(1L, "user@example.com", name = "John Doe", role = UserRole.USER.toString())
        `when`(authService.findMemberByToken(token)).thenReturn(memberResponse)

        mockMvc.perform(
            get("/api/protected/admin/top-products")
                .header("Authorization", "Bearer $token"),
        ).andExpect {
            status().isUnauthorized
        }
    }

    @Test
    fun `should return members who added items to cart in last 7 days when User role is Admin`() {
        val memberResponse = MemberResponse(1L, "user@example.com", name = "Admin", role = UserRole.ADMIN.toString())
        `when`(authService.findMemberByToken(token)).thenReturn(memberResponse)

        val activeMembers =
            listOf(
                MemberResponse(2L, "jane@example.com", name = "Jane Smith", role = UserRole.USER.toString()),
                MemberResponse(3L, "bob@example.com", name = "Bob Johnson", role = UserRole.USER.toString()),
            )

        `when`(cartService.findMembersWithCartActivityInLast7Days()).thenReturn(activeMembers)

        mockMvc.perform(
            get("/api/protected/admin/cart-activity")
                .header("Authorization", "Bearer $token"),
        ).andExpect {
            status().isOk
            jsonPath("$.size()").value(2)
            jsonPath("$[0].id").value(2)
            jsonPath("$[0].name").value("Jane Smith")
            jsonPath("$[0].email").value("jane@example.com")
            jsonPath("$[1].name").value("Bob Johnson")
        }

        verify(cartService).findMembersWithCartActivityInLast7Days()
    }

    @Test
    fun `should return status code 401 for cartactivity endpoint when User role is not Admin`() {
        val memberResponse = MemberResponse(1L, "user@example.com", name = "John Doe", role = UserRole.USER.toString())
        `when`(authService.findMemberByToken(token)).thenReturn(memberResponse)

        mockMvc.perform(
            get("/api/protected/admin/cart-activity")
                .header("Authorization", "Bearer $token"),
        ).andExpect {
            status().isUnauthorized
        }
    }
}
