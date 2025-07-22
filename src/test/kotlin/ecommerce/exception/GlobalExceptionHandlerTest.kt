package ecommerce.exception

import ecommerce.dto.ProductRequest
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = ["spring.sql.init.mode=never"],
)
class GlobalExceptionHandlerTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DROP TABLE products IF EXISTS")

        jdbcTemplate.execute(createQuery())
    }

    private fun createQuery(): String {
        return """
            create table PRODUCTS
            (
                ID       int              not null AUTO_INCREMENT,
                NAME     varchar(100)     not null,
                PRICE    double not null,
                IMAGE_URL varchar(500),
                PRIMARY KEY (ID)
            )
            """.trimIndent()
    }

    @Test
    fun `Should throw if name of product is blank`() {
        val product = ProductRequest("", 39.00, "http://www.test.com/test.jpg")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/api/products")
                .then()
                .extract()
                .response()
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.body.jsonPath().get<String>("name")).contains("Name must not be blank")
    }

    @Test
    fun `Should throw if name has more then 15 letters`() {
        val product = ProductRequest("1234567890123 56", 39.00, "http://www.test.com/test.jpg")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/api/products")
                .then()
                .extract()
                .response()
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.body.jsonPath().get<String>("name")).contains("Name must be at most 15 characters long")
    }

    @Test
    fun `Should throw if name has invalid characters`() {
        val product = ProductRequest("?", 39.00, "http://www.test.com/test.jpg")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/api/products")
                .then()
                .extract()
                .response()
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(
            response.body.jsonPath().get<String>("name"),
        ).contains("Invalid characters in name. Allowed: letters, numbers, spaces, (, ), [, ], +, -, &, /, _")
    }

    @Test
    fun `Should throw if price is 0`() {
        val product = ProductRequest("Table", 0.0, "http://www.test.com/test.jpg")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/api/products")
                .then()
                .extract()
                .response()
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(
            response.body.jsonPath().get<String>("price"),
        ).contains("Price must be greater than 0")
    }

    @Test
    fun `Should throw if price is lower`() {
        val product = ProductRequest("Table", -10.0, "http://www.test.com/test.jpg")
        val response =
            RestAssured
                .given().log().all().body(product)
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/api/products")
                .then()
                .extract()
                .response()
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(
            response.body.jsonPath().get<String>("price"),
        ).contains("Price must be greater than 0")
    }
}
