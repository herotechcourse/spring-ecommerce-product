package ecommerce.controller

import ecommerce.dto.member.LoginRequest
import ecommerce.dto.stats.ActiveMemberResponse
import ecommerce.dto.stats.TopProductStats
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql(
    scripts = ["/sql/add-cartitems.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest() {
    @LocalServerPort
    private var port: Int = 0
    private lateinit var authToken: String

    @BeforeEach
    fun setupAuthentication() {
        RestAssured.port = port

        val loginRequest = LoginRequest("admin@example.com", "Admin_passw0rd")
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
        val response =
            RestAssured.given()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/stats/top-products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
        val topProducts = response.jsonPath().getList("", TopProductStats::class.java)

        assertThat(topProducts).isNotNull
        assertThat(topProducts).isNotEmpty

        topProducts.forEach { product ->
            assertThat(product.productId).isNotNull
            assertThat(product.productName).isNotBlank
            assertThat(product.timesAdded).isGreaterThan(0)
            assertThat(product.mostRecentAddedTime).isNotNull
        }
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
        val response =
            RestAssured.given()
                .header("Authorization", "Bearer $authToken")
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/stats/active-members")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()

        val activeMembers = response.jsonPath().getList("", ActiveMemberResponse::class.java)
        assertThat(activeMembers).isNotNull
        assertThat(activeMembers).isNotEmpty
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
