package ecommerce.controller

import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

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
    }

    @Test
    fun update() {
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({
            val ps = it.prepareStatement("INSERT INTO products(product_name, price, image_url) VALUES (?, ?, ?)", arrayOf("id"))
            ps.setString(1, "Product 1")
            ps.setDouble(2, 10.2)
            ps.setString(3, "url.com")
            ps
        }, keyHolder)

        val generatedId = keyHolder.key!!.toLong()
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
                .`when`().put("/products/$generatedId")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun delete() {
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({
            val ps = it.prepareStatement("INSERT INTO products(product_name, price, image_url) VALUES (?, ?, ?)", arrayOf("id"))
            ps.setString(1, "Product 1")
            ps.setDouble(2, 10.2)
            ps.setString(3, "url.com")
            ps
        }, keyHolder)

        val generatedId = keyHolder.key!!.toLong()
        val response =
            RestAssured
                .given().log().all()
                .`when`().delete("/products/$generatedId")
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
