package ecommerce.endToEnd

import ecommerce.dto.LoginRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminStatsControllerTest {
    lateinit var token: String

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM CART_HISTORY")
        jdbcTemplate.execute("DELETE FROM CART_ITEMS")

        jdbcTemplate.execute("DELETE FROM PRODUCTS")
        jdbcTemplate.update(
            """
            INSERT INTO PRODUCTS (id, name, price, image_url)
            VALUES (1, 'test1', 2.50, 'https://test1.JPG'),
                   (2, 'test2', 3.20, 'https://test2.jpg')
            """.trimIndent(),
        )
        jdbcTemplate.execute(
            """
            INSERT INTO CART_ITEMS (CART_ID, PRODUCT_ID, QUANTITY, CREATED_AT)
            VALUES (1, 1, 3, '2025-07-27 15:00:34'),
                   (2, 2, 3, '2025-07-10 15:00:34')
            """.trimIndent(),
        )

        jdbcTemplate.execute(
            """
            INSERT INTO CART_HISTORY (CART_ID, PRODUCT_ID, STATUS, CREATED_AT)
            VALUES (1, 1,'ADDED', '2025-07-27 15:00:34'),
                   (2, 2, 'ADDED', '2025-07-10 15:00:34')
            """.trimIndent(),
        )

        val loginRequest =
            LoginRequest(
                "admin@test.com",
                "12345678",
            )

        val loginResponse =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/api/members/login")
                .then().extract()

        token = loginResponse.body().jsonPath().getString("token")
    }

    @Test
    fun getTopProducts() {
        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/admin/stats/top-products")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val products = response.body().jsonPath().getList<String>("productName")
        Assertions.assertThat(products).isNotEmpty()
        Assertions.assertThat(products.size).isEqualTo(2)
        Assertions.assertThat(products).contains("test1")
        Assertions.assertThat(products).contains("test2")
    }

    @Test
    fun getTopActiveUsers() {
        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/admin/stats/active-users")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val users = response.body().jsonPath()
        Assertions.assertThat(users.getList<Int>("memberId")).contains(1)
        Assertions.assertThat(users.getList<String>("memberEmail")).contains("admin@test.com")
        Assertions.assertThat(users.getList<String>("memberName")).contains("Admin")
    }
}
