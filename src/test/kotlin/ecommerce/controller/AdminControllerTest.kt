package ecommerce.controller

import ecommerce.dto.member.LoginRequest
import ecommerce.dto.member.RegisterRequest
import ecommerce.dto.stats.ActiveMemberResponse
import ecommerce.dto.stats.TopProductStats
import ecommerce.service.AdminService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminControllerTest() {
    @LocalServerPort
    private var port: Int = 0
    private lateinit var authToken: String

    @MockitoBean
    private lateinit var adminService: AdminService

    @BeforeEach
    fun setupAuthentication() {
        RestAssured.port = port
        val registerRequest = RegisterRequest("validEmail@email.com", "SecureP@ss1", "Test User")
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(registerRequest)
            .`when`().post("/api/members/register")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()

        val loginRequest = LoginRequest("validEmail@email.com", "SecureP@ss1")
        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .`when`().post("/api/members/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()

        authToken = response.jsonPath().getString("token")
        assertThat(authToken).isNotNull().isNotBlank()
    }

    @Test
    fun `getTopProducts with authentication should return 200 OK with data`() {
        val mockTopProducts =
            listOf(
                TopProductStats(1, "Lotion", 100, LocalDateTime.now().minusDays(3)),
                TopProductStats(2, "Cream", 80, LocalDateTime.now().minusDays(1)),
            )
        Mockito.`when`(adminService.getTop5MostAddedProducts()).thenReturn(mockTopProducts)
        val response =
            RestAssured.given()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/stats/top-products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
        val topProducts = response.jsonPath().getList("", TopProductStats::class.java)
        assertThat(topProducts).isEqualTo(mockTopProducts)
        assertThat(topProducts[0].productId).isEqualTo(mockTopProducts[0].productId)
        assertThat(topProducts[0].productName).isEqualTo(mockTopProducts[0].productName)
        assertThat(topProducts[0].timesAdded).isEqualTo(mockTopProducts[0].timesAdded)
        assertThat(topProducts[0].mostRecentAddedTime.truncatedTo(ChronoUnit.SECONDS))
            .isEqualTo(mockTopProducts[0].mostRecentAddedTime.truncatedTo(ChronoUnit.SECONDS))

        assertThat(topProducts[1].productId).isEqualTo(mockTopProducts[1].productId)
        assertThat(topProducts[1].productName).isEqualTo(mockTopProducts[1].productName)
        assertThat(topProducts[1].timesAdded).isEqualTo(mockTopProducts[1].timesAdded)
        assertThat(topProducts[1].mostRecentAddedTime.truncatedTo(ChronoUnit.SECONDS))
            .isEqualTo(mockTopProducts[1].mostRecentAddedTime.truncatedTo(ChronoUnit.SECONDS))
        Mockito.verify(adminService).getTop5MostAddedProducts()
    }

    @Test
    fun `get top products without authentication should return 403 forbidden`() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`().get("/api/admin/stats/top-products")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun `getActiveMembers with authentication should return 200 OK with data`() {
        val mockActiveMembers =
            listOf(
                ActiveMemberResponse(UUID.randomUUID(), "TestUser1", "TestUser1@example.com"),
                ActiveMemberResponse(UUID.randomUUID(), "TestUser2", "TestUser2@example.com"),
            )
        Mockito.`when`(adminService.getRecentlyActiveMembers()).thenReturn(mockActiveMembers)

        val response =
            RestAssured.given()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/stats/active-members")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()

        val activeMembers = response.jsonPath().getList("", ActiveMemberResponse::class.java)
        assertThat(activeMembers).isEqualTo(mockActiveMembers)
        Mockito.verify(adminService).getRecentlyActiveMembers()
    }

    @Test
    fun `getActiveMembers without authentication should return 403 forbidden`() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`().get("/api/admin/stats/active-members")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value())
    }
}
