package ecommerce.controller

import ecommerce.auth.JwtProvider
import ecommerce.model.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartControllerTest {
    @Test
    fun `addToCart() should return '401' response when user is not authorized with a token`() {
        val token = JwtProvider.generateToken("invalid@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().post("/api/cart")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `addToCart() should return '200' response when cart item is added`() {
        val token = JwtProvider.generateToken("user@test.com")
        val requestBody = mapOf("productId" to 1)

        var response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .`when`().post("/api/cart")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())

        var responseJson = response.jsonPath()

        assertThat(responseJson.getString("id")).isEqualTo("4")
        assertThat(responseJson.getString("memberId")).isEqualTo("2")
        assertThat(responseJson.getString("productId")).isEqualTo("1")
        assertThat(responseJson.getString("quantity")).isEqualTo("1")

        response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .`when`().post("/api/cart")
                .then().log().all().extract()

        responseJson = response.jsonPath()

        assertThat(responseJson.getString("id")).isEqualTo("4")
        assertThat(responseJson.getString("memberId")).isEqualTo("2")
        assertThat(responseJson.getString("productId")).isEqualTo("1")
        assertThat(responseJson.getString("quantity")).isEqualTo("2")
    }

    @Test
    fun `deleteCartItem() should return '401' response when user is not authorized with a token`() {
        val token = JwtProvider.generateToken("invalid@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().delete("/api/cart/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `deleteCartItem() should return '200' response when cart item is deleted`() {
        val token = JwtProvider.generateToken("user@test.com")
        val requestBody = mapOf("productId" to 1)

        var response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .`when`().post("/api/cart")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())

        response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().delete("/api/cart/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())

        response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .`when`().get("/api/cart")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `deleteCartItem() should return '200' response when cart item does not exist`() {
        val token = JwtProvider.generateToken("user@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().delete("/api/cart/products/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `getCartItems() should return '200' response and user cart items`() {
        val token = JwtProvider.generateToken("admin@test.com")

        var response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().get("/api/cart")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())

        val responseJson = response.jsonPath()
        val cartItems = responseJson.getList("items", Product::class.java)

        assertThat(cartItems).hasSize(3)

        // First cart item
        assertThat(responseJson.getString("[0].memberId")).isEqualTo("1")
        assertThat(responseJson.getString("[0].productId")).isEqualTo("1")
        assertThat(responseJson.getString("[0].quantity")).isEqualTo("2")

        // Second cart item
        assertThat(responseJson.getString("[1].memberId")).isEqualTo("1")
        assertThat(responseJson.getString("[1].productId")).isEqualTo("2")
        assertThat(responseJson.getString("[1].quantity")).isEqualTo("1")

        // Third cart item
        assertThat(responseJson.getString("[2].memberId")).isEqualTo("1")
        assertThat(responseJson.getString("[2].productId")).isEqualTo("3")
        assertThat(responseJson.getString("[2].quantity")).isEqualTo("3")
    }

    @Test
    fun `getCartItems() should return '401' response when user is not authorized with a token`() {
        val token = JwtProvider.generateToken("invalid@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().get("/api/cart")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}
