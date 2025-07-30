package ecommerce.controller

import ecommerce.controller.api.CartController
import ecommerce.dto.AuthResponse
import ecommerce.dto.CartAddItemForm
import ecommerce.dto.CartUpdateQuantityForm
import ecommerce.dto.LoginForm
import ecommerce.exception.NotFoundException
import ecommerce.model.Member
import ecommerce.service.CartService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = ["/sql/member.sql", "/sql/product.sql", "/sql/cart_item.sql", "/sql/cart_item_event.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
)
class CartControllerTest(
    @Autowired private val controller: CartController,
) {
    @Test
    fun addToCart() {
        val productId = PRODUCT_ID
        val quantity = 1
        val form = CartAddItemForm(productId, quantity)
        val expected = CartService.MESSAGE_ADD_SUCCESS
        val response = controller.addToCart(form, LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `addToCart() - return 200 OK when credential is valid`() {
        val productId = PRODUCT_ID
        val quantity = 1
        val expected = CartService.MESSAGE_ADD_SUCCESS

        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(LOGIN_EMAIL, LOGIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $accessToken")
            .body(CartAddItemForm(productId, quantity))
            .contentType(ContentType.JSON)
            .`when`().post("/api/cart")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.OK.value())
            .body(equalTo(expected))
    }

    @Test
    fun `addToCart() - return 401 Unauthorized when credential is invalid`() {
        val productId = PRODUCT_ID
        val quantity = 1

        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(LOGIN_EMAIL, LOGIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        val contaminatedToken = accessToken + 123
        RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $contaminatedToken")
            .body(CartAddItemForm(productId, quantity))
            .contentType(ContentType.JSON)
            .`when`().post("/api/cart")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Form validation failure when 'productId' is blank`() {
        val productId = 0L
        val quantity = 1
        val expected = "Product ID is missing"
        RestAssured
            .given().log().all()
            .body(CartAddItemForm(productId, quantity))
            .contentType(ContentType.JSON)
            .`when`().post("/api/cart")
            .then().log().all()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors.productId", equalTo(expected))
    }

    @Test
    fun `Form validation failure when 'quantity' is less than 1`() {
        val productId = PRODUCT_ID
        val quantity = 0
        val expected = "Product quantity is too small"
        RestAssured
            .given().log().all()
            .body(CartAddItemForm(productId, quantity))
            .contentType(ContentType.JSON)
            .`when`().post("/api/cart")
            .then().log().all()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors.quantity", equalTo(expected))
    }

    @Test
    fun `viewCart() - empty cart`() {
        val expected = 0
        val response = controller.viewCart(LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.size).isEqualTo(expected)
    }

    @Test
    fun `viewCart() - 1 item in cart`() {
        addToCart()
        val expected = 1
        val response = controller.viewCart(LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.size).isEqualTo(expected)
    }

    @Test
    fun `removeFromCart() - return 200 OK when remove success`() {
        addToCart()
        val productId = PRODUCT_ID
        val expected = CartService.MESSAGE_REMOVE_SUCCESS
        val response = controller.removeFromCart(productId, LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `removeFromCart() - throws exception when nothing to remove`() {
        val productId = PRODUCT_ID
        assertThrows<NotFoundException> { controller.removeFromCart(productId, LOGIN_MEMBER) }
    }

    @Test
    fun `updateQuantity() - return 200 OK when update success`() {
        addToCart()
        val productId = PRODUCT_ID
        val quantity = 10
        val form = CartUpdateQuantityForm(quantity)
        val expected = CartService.MESSAGE_UPDATE_SUCCESS
        val response = controller.updateQuantity(productId, form, LOGIN_MEMBER)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expected)
    }

    @Test
    fun `updateQuantity() - throws exception when nothing to update`() {
        val productId = PRODUCT_ID
        val quantity = 10
        val form = CartUpdateQuantityForm(quantity)
        assertThrows<NotFoundException> { controller.updateQuantity(productId, form, LOGIN_MEMBER) }
    }

    @Test
    fun `Interceptor - allow admin user to access admin endpoint`() {
        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(LOGIN_EMAIL, LOGIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $accessToken")
            .contentType(ContentType.JSON)
            .`when`().get("/api/admin/cart-stats/top5-products")
            .then().log().all()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `Interceptor - block non-admin user to access admin endpoint`() {
        val accessToken =
            RestAssured
                .given().log().all()
                .body(LoginForm(NON_ADMIN_EMAIL, NON_ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/login")
                .then().log().all().extract().`as`(AuthResponse::class.java).accessToken

        RestAssured
            .given().log().all()
            .header("Authorization", "Bearer $accessToken")
            .contentType(ContentType.JSON)
            .`when`().get("/api/admin/cart-stats/top5-products")
            .then().log().all()
            .assertThat()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    companion object {
        private const val PRODUCT_ID = 1L
        private const val LOGIN_EMAIL = "san@htc.com"
        private const val LOGIN_PASSWORD = "san1234"
        private const val NON_ADMIN_EMAIL = "min@htc.com"
        private const val NON_ADMIN_PASSWORD = "min1234"
        private val LOGIN_MEMBER = Member(1L, LOGIN_EMAIL, LOGIN_PASSWORD)
    }
}
