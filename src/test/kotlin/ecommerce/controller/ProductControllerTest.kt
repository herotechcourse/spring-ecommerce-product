package ecommerce.controller

import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository(jdbcTemplate)
        jdbcTemplate.execute(
            """
            CREATE TABLE IF NOT EXISTS products (
            id INT PRIMARY KEY AUTO_INCREMENT,
            product_name VARCHAR(255) NOT NULL,
            price DOUBLE CHECK (price >= 0),
            image_url VARCHAR(255))
            """.trimIndent(),
        )
        jdbcTemplate.update(
            "INSERT INTO products(product_name,price,image_url) VALUES (?,?,?)",
            "Product 1",
            10.2,
            "url.com",
        )
    }

    @Test
    fun create() {
        val product =
            Product(
                name = "Product 1",
                price = 10.0,
                imageUrl = "http://localhost:8080/image/upload/product1.jpg",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(product)
                .contentType(ContentType.JSON)
                .`when`().post("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `Returns Products`() {
        val response =
            RestAssured
                .given().log().all()
                .`when`().get("/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        AssertionsForClassTypes.assertThat(response.asString()).contains("Product 1")
    }

    @Test
    fun update() {
        val response =
            RestAssured
                .given().log().all()
                .body(
                    Product(
                        name = "Product 2",
                        price = 10.0,
                        imageUrl = "http://localhost:8080/image/upload/product1.jpg",
                    ),
                )
                .contentType(ContentType.JSON)
                .`when`().put("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }


    @Test
    fun delete() {
        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `Throws NotFoundException if No id provided`() {
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/products/")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
