package ecommerce.controller

import ecommerce.dto.CartItemRequest
import ecommerce.dto.RegistrationRequest
import ecommerce.repository.CartItemRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class CartControllerTest {
    lateinit var token: String

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var cartItemRepository: CartItemRepository

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute(
            """
            INSERT INTO PRODUCTS (ID, NAME, PRICE, IMAGE_URL)
            VALUES (11, 'test', 11.90, 'http://www.test.jpg')
            """.trimIndent(),
        )

        val registrationRequest =
            RegistrationRequest(
                "test",
                "test@test.com",
                "12345678",
            )

        token =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(registrationRequest)
                .post("/api/members/register")
                .then().extract().body().jsonPath().getString("token")
    }

    @Test
    fun addToCart() {
        val productToCart =
            CartItemRequest(
                productId = 11,
                quantity = 2,
            )

        val addProduct =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productToCart)
                .`when`().post("/api/user/wishes")
                .then().log().all().extract()

        assertThat(addProduct.statusCode()).isEqualTo(HttpStatus.OK.value())
    }
}
