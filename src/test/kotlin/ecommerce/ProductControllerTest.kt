package ecommerce

import ecommerce.product.domain.Product
import ecommerce.product.repository.ProductRepository
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
        jdbcTemplate.execute("DROP TABLE CART_ITEMS IF EXISTS")
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

        val expectedProducts =
            listOf(
                Product(
                    id = 1,
                    name = "Coca-Cola",
                    price = 2.00,
                    imageUrl = "https://mcdonalds.com.mt/wp-content/uploads/2024/06/COCA-COLA-WEBSITE-IMG.jpg",
                ),
                Product(
                    id = 2,
                    name = "Fanta",
                    price = 2.50,
                    imageUrl =
                        "https://www.cokesolutions.com/content/dam/cokesolutions/us/images/Products/" +
                            "Fanta-Orange-PET.jpg",
                ),
                Product(
                    id = 3,
                    name = "Cappuccino",
                    price = 4.39,
                    imageUrl =
                        "https://www.tchibo.de/kaffeeakademie/media/pages/global-images/" +
                            "fb95bb5370-1729609446/adobestock_219364830-1440x700-crop-42-46.jpg",
                ),
                Product(
                    id = 4,
                    name = "Tea",
                    price = 1.59,
                    imageUrl = "https://cupitol.com/wp-content/uploads/2019/08/tea-drinking-1.jpg",
                ),
            )

        assertThat(products).isEqualTo(expectedProducts)
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

//    @Test
//    fun `create product with blank name should return 400`() {
//        val response = RestAssured.given()
//            .log().all()
//            .contentType(ContentType.JSON)
//            .body(Product(name = "", price = 2.50, imageUrl = "https://sprite.jpg"))
//            .`when`()
//            .post("/products")
//            .then()
//            .log().all()
//            .extract()
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
//        assertThat(response.jsonPath().getString("name")).isEqualTo("Product name cannot be blank")
//    }

    @Test
    fun `create product with name longer than 15 characters should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(Product(name = "Very Long Product Name", price = 2.50, imageUrl = "https://sprite.jpg"))
                .`when`()
                .post("/products")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("name")).isEqualTo("Product name must be 15 characters or less")
    }

    @Test
    fun `create product with invalid name characters should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(Product(name = "Cola#Invalid", price = 2.50, imageUrl = "https://example.com/sprite.jpg"))
                .`when`()
                .post("/products")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("name")).isEqualTo(
            "Product name can only contain letters, numbers, spaces, and allowed special characters: (), [], +, -, &, /, _",
        )
    }

    @Test
    fun `create product with non-unique name should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(Product(name = "Coca-Cola", price = 2.50, imageUrl = "https://cola.jpg"))
                .`when`()
                .post("/products")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Product name must be unique")
    }

    @Test
    fun `create product with non-positive price should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(Product(name = "Sprite", price = 0.0, imageUrl = "https://sprite.jpg"))
                .`when`()
                .post("/products")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("price")).isEqualTo("Price must be greater than 0")
    }

    @Test
    fun `create product with invalid image URL should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(Product(name = "Sprite", price = 2.50, imageUrl = "ftgeh4iugp://sprite.jpg"))
                .`when`()
                .post("/products")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("imageUrl")).isEqualTo("Image URL must start with http:// or https://")
    }

    @Test
    fun `update product with non-unique name should return 400`() {
        val response =
            RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(Product(name = "Fanta", price = 2.50, imageUrl = "https://example.com/fanta.jpg"))
                .`when`()
                .put("/products/1")
                .then()
                .log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.jsonPath().getString("error")).isEqualTo("Product name must be unique")
    }
}
