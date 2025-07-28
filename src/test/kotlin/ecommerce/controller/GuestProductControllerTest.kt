package ecommerce.controller

import io.restassured.RestAssured
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
class GuestProductControllerTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM cart_history")
        jdbcTemplate.execute("DELETE FROM cart_items")
        jdbcTemplate.execute("DELETE FROM carts")
        jdbcTemplate.execute("DELETE FROM products")
        jdbcTemplate.execute(
            """
        INSERT INTO products (id, name, price, image_url)
        VALUES (1, 'Espresso', 2.50, 'http://image.test/espresso.jpg')
        """,
        )
    }

    @Test
    fun getProducts() {
        val response =
            RestAssured
                .given().log().all()
                .`when`()
                .request("GET", "/api/products")
                .then()
                .extract()
                .response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        val products = response.body().jsonPath().getList<String>("")
        assertThat(products).isNotEmpty()
        assertThat(products.size).isEqualTo(1)
    }

    @Test
    fun getProduct() {
        val response =
            RestAssured
                .given().log().all()
                .`when`()
                .request("GET", "/api/products/1")
                .then()
                .extract()
                .response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
        val productName = response.body().jsonPath().getString("name")
        assertThat(productName).isEqualTo("Espresso")
    }
}
