package ecommerce.controller

import ecommerce.TestSecurityConfig
import ecommerce.dto.ProductRequest
import ecommerce.entity.Product
import ecommerce.repository.ProductRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-security")
@Import(TestSecurityConfig::class)
class CRUDTest {
    private lateinit var productRepository: ProductRepository

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE products IF EXISTS")
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS products(" +
                "id BIGINT AUTO_INCREMENT, name VARCHAR(255) NOT NULL, price DOUBLE NOT NULL, image_url VARCHAR(512) NOT NULL)",
        )

        val products =
            listOf(
                Product(
                    id = 1L,
                    name = "vanilla ice",
                    price = 1.99,
                    imageUrl = "https://laurenslatest.com/wp-content/uploads/2020/08/vanilla-ice-cream-5-copy-360x361.jpg",
                ),
                Product(
                    id = 2L,
                    name = "pistachio ice",
                    price = 2.49,
                    imageUrl = "https://greenhealthycooking.com/wp-content/uploads/2017/06/Pistachio-Ice-Cream-Photo.jpg",
                ),
                Product(
                    id = 3L,
                    name = "chocolate ice",
                    price = 1.49,
                    imageUrl = "https://www.cravethegood.com/wp-content/uploads/2021/04/sous-vide-chocolate-ice-cream-15.jpg",
                ),
            )

        jdbcTemplate.batchUpdate(
            "INSERT INTO products(name, price, image_url) VALUES (?, ?, ?)",
            products,
            products.size,
        ) { ps, product ->
            ps.setString(1, product.name)
            ps.setDouble(2, product.price)
            ps.setString(3, product.imageUrl)
        }
    }

    @Test
    fun create() {
        val request =
            ProductRequest(
                name = "orange ice",
                price = 2.80,
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRf8Fdb-33SCOszWX_UF-92pCwX4Rcam0uVCg&s",
            )

        val response =
            RestAssured
                .given().log().all()
                .port(port)
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun read() {
        val response =
            RestAssured
                .given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .`when`().get("/api/products")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        Assertions.assertThat(response.jsonPath().getList("", Product::class.java)).hasSize(3)
    }

    @Test
    fun update_success() {
        val response =
            RestAssured
                .given().log().all()
                .port(port)
                .body(
                    ProductRequest(
                        name = "lemon ice",
                        price = 3.60,
                        imageUrl =
                            "https://www.carnation.co.uk/sites/default/files/2020" +
                                "-05/Final%20Lemon%20Curd%20Ice%20Cream%20mobile.jpg",
                    ),
                )
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/1")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun update_failure() {
        val response =
            RestAssured
                .given().log().all()
                .port(port)
                .body(
                    ProductRequest(
                        name = "vanilla ice",
                        price = 3.60,
                        imageUrl = "https://laurenslatest.com/wp-content/uploads/2020/08/vanilla-ice-cream-5-copy-360x361.jpg",
                    ),
                )
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/4")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun delete_success() {
        create()

        val response =
            RestAssured
                .given().log().all()
                .port(port)
                .`when`().delete("/api/products/1")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun delete_failure() {
        val response =
            RestAssured
                .given().log().all()
                .port(port)
                .`when`().delete("/api/products/4")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }
}
