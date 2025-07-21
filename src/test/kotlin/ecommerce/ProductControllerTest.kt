package ecommerce

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DROP TABLE products IF EXISTS")
        jdbcTemplate.execute(
            "CREATE TABLE products(" + "id SERIAL, name VARCHAR(100), price DECIMAL(10,2), image_url VARCHAR(500))",
        )

        val splitUpAttributes: List<Array<String>> =
            listOf(
                "Coca-Cola 2.00 https://mcdonalds.com.mt/wp-content/uploads/2024/06/" +
                    "COCA-COLA-WEBSITE-IMG.jpg",
                "Fanta 2.50 https://www.cokesolutions.com/content/dam/cokesolutions/us/" +
                    "images/Products/Fanta-Orange-PET.jpg",
                "Cappuccino 4.39 https://www.tchibo.de/kaffeeakademie/media/pages/global-images/" +
                    "fb95bb5370-1729609446/adobestock_219364830-1440x700-crop-42-46.jpg",
                "Tea 1.59 https://cupitol.com/wp-content/uploads/2019/08/tea-drinking-1.jpg",
            ).map { name -> name.split(" ").toTypedArray() }.toList()
        jdbcTemplate.batchUpdate("INSERT INTO products(name, price, image_url) VALUES (?,?,?)", splitUpAttributes)
    }

    @Test
    fun create() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(Product(name = "cola", price = 4.50, imageUrl = "https://cola.jpg"))
                .`when`()
                .post("/products")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun read() {
        val products = productRepository.findAllProducts()
        assertThat(products.size).isEqualTo(4)
        with(products[0]) {
            assertThat(id).isEqualTo(1)
            assertThat(name).isEqualTo("Coca-Cola")
            assertThat(price).isEqualTo(2.00)
            assertThat(imageUrl).isEqualTo("https://mcdonalds.com.mt/wp-content/uploads/2024/06/COCA-COLA-WEBSITE-IMG.jpg")
        }
        with(products[1]) {
            assertThat(id).isEqualTo(2)
            assertThat(name).isEqualTo("Fanta")
            assertThat(price).isEqualTo(2.50)
            assertThat(
                imageUrl,
            ).isEqualTo(
                "https://www.cokesolutions.com/content/dam/cokesolutions/us/images/Products/" +
                    "Fanta-Orange-PET.jpg",
            )
        }
        with(products[2]) {
            assertThat(id).isEqualTo(3)
            assertThat(name).isEqualTo("Cappuccino")
            assertThat(price).isEqualTo(4.39)
            assertThat(
                imageUrl,
            ).isEqualTo(
                "https://www.tchibo.de/kaffeeakademie/media/pages/global-images/fb95bb5370-1729609446/" +
                    "adobestock_219364830-1440x700-crop-42-46.jpg",
            )
        }
        with(products[3]) {
            assertThat(id).isEqualTo(4)
            assertThat(name).isEqualTo("Tea")
            assertThat(price).isEqualTo(1.59)
            assertThat(imageUrl).isEqualTo("https://cupitol.com/wp-content/uploads/2019/08/tea-drinking-1.jpg")
        }
    }

    @Test
    fun `update existing product`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(
                    Product(
                        name = "Fanta-new",
                        price = 5.60,
                        imageUrl =
                            "https://www.cokesolutions.com/content/dam/cokesolutions/us/images/" +
                                "Products/Fanta-Orange-PET.jpg",
                    ),
                )
                .`when`()
                .put("/products/1")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun delete() {
        val response =
            RestAssured.given()
                .log().all()
                .`when`()
                .delete("/products/1")
                .then()
                .log().all()
                .extract()
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }
}
