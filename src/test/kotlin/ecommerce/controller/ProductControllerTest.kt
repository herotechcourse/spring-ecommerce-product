package ecommerce.controller

import ecommerce.domain.Product
import ecommerce.dto.member.MemberLoginRequest
import ecommerce.dto.member.MemberRegisterRequest
import ecommerce.dto.product.CreateProductRequest
import ecommerce.dto.product.ProductResponse
import ecommerce.dto.product.UpdateProductRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var authService: ecommerce.service.AuthService

    private val testUserName = "bo"
    private val testEmail = "bo@gmail.com"
    private val testPassword = "MojataBebushkaAngie10%"
    private lateinit var testAccessToken: String

    private fun createProductHelper(
        name: String,
        price: Double,
        img: String,
        quantity: Int
    ): ProductResponse {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $testAccessToken")
            .body(CreateProductRequest(name = name, price = price, img = img, quantity = quantity))
            .contentType(ContentType.JSON)
            .`when`().post("/api/products")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.CREATED.value())
            .extract().response()
            .`as`(ProductResponse::class.java)
    }

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        jdbcTemplate.execute("delete from products")
        jdbcTemplate.execute("delete from members")
        jdbcTemplate.execute("delete from carts")
        jdbcTemplate.execute("delete from cart_items")

        val registerRequest = MemberRegisterRequest(testUserName, testEmail, testPassword)
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(registerRequest)
            .`when`().post("/api/members/register")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())

        val loginRequest = MemberLoginRequest(testEmail, testPassword)
        val loginResponse =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .`when`().post("/api/members/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()

        testAccessToken = loginResponse.jsonPath().getString("token")
    }

    @Test
    fun `it should create a product with valid authentication`() {
        val createdProduct =
            createProductHelper(
                name = "lotion",
                price = 20.0,
                img = "http://lotion.jpeg",
                quantity = 1,
            )

        assertThat(createdProduct.id).isGreaterThan(0)
        assertThat(createdProduct.name).isEqualTo("lotion")
        assertThat(createdProduct.price).isEqualTo(20.0)
        assertThat(createdProduct.img).isEqualTo("http://lotion.jpeg")
        assertThat(createdProduct.quantity).isEqualTo(1)
    }

    @Test
    fun `it should retrieve a product by it's id with valid authentication`() {
        val createdProduct = createProductHelper(name = "lotion", price = 20.0, img = "http://lotion.jpeg", quantity = 1,)
        val productId = createdProduct.id

        val response = RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $testAccessToken")
            .`when`().get("/api/products/{id}", productId)
            .then().log().all()
            .assertThat().statusCode(HttpStatus.OK.value())
            .extract()

        val productResponse = response.`as`(ProductResponse::class.java)
        assertThat(productResponse.id).isEqualTo(productId)
        assertThat(productResponse.name).isEqualTo("lotion")
    }

    @Test
    fun `it should return all the products with valid authentication`() {
        val createdProduct1 = createProductHelper(name = "lotion", price = 20.0, img = "http://lotion.jpeg", quantity = 1,)
        val createdProduct2 = createProductHelper(name = "lotion2", price = 20.0, img = "http://lotion2.jpeg", quantity = 2,)

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $testAccessToken")
                .`when`().get("/api/products")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract()

        assertThat(response.jsonPath().getList<Product>(""))
    }

    @Test
    fun `it should update a product with valid authentication`() {
        val createdProduct = createProductHelper(name = "lotion", price = 10.0, img = "http://lotion.jpg", quantity = 5)
        val updateRequest = UpdateProductRequest(name = "lotion2", price = 15.0, img = "http://lotio2.jpg", quantity = 3)

        val response = RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $testAccessToken")
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .`when`().put("/api/products/{id}", createdProduct.id)
            .then().log().all()
            .assertThat().statusCode(HttpStatus.OK.value())
            .extract()

        val updatedProductResponse = response.`as`(ProductResponse::class.java)

        assertThat(updatedProductResponse.id).isEqualTo(createdProduct.id)
        assertThat(updatedProductResponse.name).isEqualTo(updateRequest.name)
        assertThat(updatedProductResponse.price).isEqualTo(updateRequest.price)
        assertThat(updatedProductResponse.img).isEqualTo(updateRequest.img)
        assertThat(updatedProductResponse.quantity).isEqualTo(updateRequest.quantity)
    }

    @Test
    fun `it should delete a product by its id with valid authentication`() {
        val createdProduct = createProductHelper(name = "lotion", price = 50.0, img = "http://lotion.jpg", quantity = 1,)

        val response = RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $testAccessToken")
            .`when`().delete("/api/products/{id}", createdProduct.id)
            .then().log().all()
            .assertThat().statusCode(HttpStatus.NO_CONTENT.value())
            .extract()

        RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $testAccessToken")
            .`when`().get("/api/products/{id}", createdProduct.id)
            .then().log().all()
            .assertThat().statusCode(HttpStatus.NOT_FOUND.value())
    }
}
