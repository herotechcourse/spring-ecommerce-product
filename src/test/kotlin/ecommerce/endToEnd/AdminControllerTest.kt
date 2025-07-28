package ecommerce.endToEnd

import ecommerce.dto.LoginRequest
import ecommerce.dto.ProductRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {
    lateinit var token: String

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DROP TABLE CART_HISTORY IF EXISTS")
        jdbcTemplate.execute("DROP TABLE CART_ITEMS IF EXISTS")
        jdbcTemplate.execute("DROP TABLE CARTS IF EXISTS")
        jdbcTemplate.execute("DROP TABLE PRODUCTS IF EXISTS")
        jdbcTemplate.execute(createProductTable())

        val sql =
            """
            INSERT INTO PRODUCTS (name, price, image_url)
            VALUES
            ('test', 10.99, 'https://www.test.jpg'),
            ('test2', 6.99, 'https://www.test.jpg');
            """.trimIndent()

        jdbcTemplate.execute(sql)

        val loginRequest =
            LoginRequest(
                "admin@test.com",
                "12345678",
            )

        val response =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/api/members/login")
                .then().extract()

        token = response.body().jsonPath().getString("token")
    }

    private fun createProductTable(): String {
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
    fun getAllProducts() {
        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/admin/products")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val names = response.body().jsonPath().getList<String>("")
        Assertions.assertThat(names).isNotEmpty()
        Assertions.assertThat(names.size).isEqualTo(2)
    }

    @Test
    fun getProductById() {
        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/admin/products/1")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val productName = response.body().jsonPath().getString("name")
        Assertions.assertThat(productName).isEqualTo("test")
    }

    @Test
    fun createProduct() {
        val productRequest =
            ProductRequest(
                "newProductTest",
                2.99,
                "http://www.newProduct.jpg",
            )

        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(productRequest)
                .post("/api/admin/products")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())

        val productId3 =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/admin/products/3")
                .then().log().all().extract()

        val productName = productId3.body().jsonPath().getString("name")
        Assertions.assertThat(productName).isEqualTo("newProductTest")
    }

    @Test
    fun updateProduct() {
        val productRequest =
            ProductRequest(
                "updatedTest",
                2.99,
                "http://www.newProduct.jpg",
            )

        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(productRequest)
                .put("/api/admin/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())

        val updated =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/admin/products/1")
                .then().log().all().extract()

        val productName = updated.body().jsonPath().getString("name")
        assertThat(productName).isEqualTo("updatedTest")
    }

    @Test
    fun deleteProduct() {
        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .delete("/api/admin/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())

        val deleted =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/admin/products/1")
                .then().log().all().extract()

        assertThat(deleted.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
