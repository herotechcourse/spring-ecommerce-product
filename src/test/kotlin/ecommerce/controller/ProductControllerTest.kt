package ecommerce.controller

import ecommerce.dto.ProductRequest
import ecommerce.model.Product
import ecommerce.repository.ProductResponse
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
class ProductControllerTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DROP TABLE products IF EXISTS")

        jdbcTemplate.execute(createQuery())

        val product1 = ProductRequest("cafe", 39.00, "www.test")
        val product2 = ProductRequest("table", 39.00, "www.test")
        val product3 = ProductRequest("chair", 39.00, "www.test")
        RestAssured
            .given().log().all().body(product1)
            .contentType(ContentType.JSON)
            .`when`()
            .request("POST", "/api/products")
            .then()
            .extract()
            .response()
        RestAssured
            .given().log().all().body(product2)
            .contentType(ContentType.JSON)
            .`when`()
            .request("POST", "/api/products")
            .then()
            .extract()
            .response()
        RestAssured
            .given().log().all().body(product3)
            .contentType(ContentType.JSON)
            .`when`()
            .request("POST", "/api/products")
            .then()
            .extract()
            .response()
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
    fun addProduct() {
        val response =
            RestAssured
                .given().log().all().body("""{ "name": "Screen", "price": 190.0, "imageUrl": "ciao" }""")
                .contentType(ContentType.JSON)
                .`when`()
                .request("POST", "/api/products")
                .then()
                .extract()
                .response()

        val newProduct =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`()
                .request("GET", "/api/products/4")
                .then()
                .extract()
                .response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED.value())
        assertThat(newProduct.`as`(Product::class.java).name).isEqualTo("Screen")
    }

    @Test
    fun getProducts() {
        val response =
            RestAssured
                .given().log().all()
                .`when`()
                .request("GET", "/products")
                .then()
                .extract()
                .response()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK.value())
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
        assertThat(response.`as`(ProductResponse::class.java).id).isEqualTo(1L)
        assertThat(response.`as`(ProductResponse::class.java).name).isEqualTo("cafe")
        assertThat(response.`as`(ProductResponse::class.java).price).isEqualTo(39.00)
        assertThat(response.`as`(ProductResponse::class.java).imageUrl).isEqualTo("www.test")
    }

    @Test
    fun deleteProduct() {
        val delete =
            RestAssured
                .given().log().all()
                .`when`()
                .request("DELETE", "/api/products/1")
                .then()
                .extract()
                .response()

        val after =
            RestAssured
                .given().log().all()
                .`when`()
                .request("GET", "/api/products/1")
                .then()
                .extract()
                .response()

        assertThat(delete.statusCode).isEqualTo(HttpStatus.OK.value())
        assertThat(after.statusCode).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
