package ecommerce.controller

import ecommerce.domain.Cart
import ecommerce.domain.Member
import ecommerce.domain.Product
import ecommerce.dto.cart.AddToCartRequest
import ecommerce.dto.cart.CartResponse
import ecommerce.dto.member.AuthResponse
import ecommerce.dto.member.MemberLoginRequest
import ecommerce.repository.CartEventRepository
import ecommerce.repository.CartItemRepository
import ecommerce.repository.CartRepository
import ecommerce.repository.MemberRepository
import ecommerce.repository.ProductRepository
import ecommerce.service.AuthService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Cart Api Controller Integration Tests")
class CartApiControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var cartEventRepository: CartEventRepository

    @Autowired
    private lateinit var cartRepository: CartRepository

    @Autowired
    private lateinit var cartItemRepository: CartItemRepository

    private val userEmail = "test-member-${System.nanoTime()}@gmail.com"
    private val userPassword = "MojataBebushkaAngie10%"
    private lateinit var userToken: String
    private lateinit var userMember: Member

    private val adminEmail = "admin-member-${System.nanoTime()}@gmail.com"
    private val adminPassword = "AdminPasswordStrong10%"
    private lateinit var adminToken: String
    private lateinit var adminMember: Member

    private fun registerAndLogin(
        userName: String,
        email: String,
        password: String,
        role: String,
    ): Pair<String, Member> {
        authService.registerMember(
            userName = userName,
            email = email,
            password = password,
            role = role,
        )
        val member = memberRepository.findByEmail(email)!!

        val loginRequest = MemberLoginRequest(email = email, password = password)
        val loginResponse =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .`when`().post("/api/members/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().`as`(AuthResponse::class.java)
        return Pair(loginResponse.token, member)
    }

    private fun createTestProduct(
        name: String = "lotion",
        price: Double = 20.0,
        quantity: Int = 10,
    ): Product {
        val product = Product(name = name, price = price, img = "http://img.png", quantity = quantity)
        productRepository.create(product)
        return product
    }

    private fun createTestCart(memberId: Long): Cart {
        val cart = Cart(memberId = memberId, createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        cartRepository.create(cart)
        return cart
    }

    private fun createTestCartItem(
        cartId: Long,
        productId: Long,
        quantity: Int,
    ) {
        val cartItem = ecommerce.domain.CartItem(cartId = cartId, productId = productId, quantity = quantity)
        cartItemRepository.create(cartItem)
    }

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        jdbcTemplate.execute("delete from cart_events")
        jdbcTemplate.execute("delete from cart_items")
        jdbcTemplate.execute("delete from carts")
        jdbcTemplate.execute("delete from products")
        jdbcTemplate.execute("delete from members")

        val (uToken, uMember) = registerAndLogin("bo", userEmail, userPassword, "USER")
        userToken = uToken
        userMember = uMember
        val (aToken, aMember) = registerAndLogin("admin", adminEmail, adminPassword, "ADMIN")
        adminToken = aToken
        adminMember = aMember
    }

    @DisplayName("Integration Tests for Cart Retrieval (GET /api/cart)")
    @Test
    fun `Should return 200 OK with no cart contents for a valid USER token`() {
        val response =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $userToken")
                .`when`().get("/api/cart")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().`as`(CartResponse::class.java)

        assertThat(response.memberId).isEqualTo(userMember.userId)
        assertThat(response.items).isEmpty()
        assertThat(response.totalPrice).isEqualTo(0.0)
        assertThat(response.totalQuantity).isEqualTo(0)
    }

    @Test
    fun `Should return 200 OK with cart contents for a valid USER token`() {
        val product1 = createTestProduct("lotion", 10.0, 50)
        val product2 = createTestProduct("spf", 10.0, 50)

        val userCart = createTestCart(userMember.userId)
        createTestCartItem(userCart.id, product1.id, 2)
        createTestCartItem(userCart.id, product2.id, 1)

        val response =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $userToken")
                .`when`().get("/api/cart")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().`as`(CartResponse::class.java)

        assertThat(response.memberId).isEqualTo(userMember.userId)
        assertThat(response.items).hasSize(2)
        assertThat(response.totalPrice).isEqualTo(30.0)
        assertThat(response.totalQuantity).isEqualTo(3)

        val item1 = response.items.find { it.productId == product1.id }
        assertThat(item1?.productName).isEqualTo(product1.name)
        assertThat(item1?.quantity).isEqualTo(2)

        val item2 = response.items.find { it.productId == product2.id }
        assertThat(item2?.productName).isEqualTo(product2.name)
        assertThat(item2?.quantity).isEqualTo(1)
    }

    @Test
    fun `Should return 401 Unauthorized for GET Cart with no token`() {
        RestAssured.given().log().all()
            .`when`().get("/api/cart")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 401 Unauthorized for GET Cart with an invalid USER token`() {
        RestAssured.given().log().all()
            .header("Authorization", "Bearer Token")
            .`when`().get("/api/cart")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 200 OK and update cart for a valid USER token`() {
        val product = createTestProduct("lotion", 20.0, 20)
        val requestBody = AddToCartRequest(productId = product.id, quantity = 5)

        val response =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $userToken")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .`when`().post("/api/cart")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().`as`(CartResponse::class.java)

        assertThat(response.memberId).isEqualTo(userMember.userId)
        assertThat(response.items).hasSize(1)
        assertThat(response.items[0].productId).isEqualTo(product.id)
        assertThat(response.items[0].quantity).isEqualTo(5)
        assertThat(response.totalPrice).isEqualTo(100.0)
    }

    @DisplayName("Integration Tests for Adding Products to Cart (POST /api/cart/items)")
    @Test
    fun `should return 200 OK and update quantity if product already in cart`() {
        val product = createTestProduct("lotion", 20.0, 20)
        val userCart = createTestCart(userMember.userId)
        createTestCartItem(userCart.id, product.id, 3)

        val requestBody = AddToCartRequest(productId = product.id, quantity = 5)

        val response =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $userToken")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .`when`().post("/api/cart")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().`as`(CartResponse::class.java)

        assertThat(response.memberId).isEqualTo(userMember.userId)
        assertThat(response.items).hasSize(1)
        assertThat(response.items[0].productId).isEqualTo(product.id)
        assertThat(response.items[0].quantity).isEqualTo(8)
        assertThat(response.totalPrice).isEqualTo(160.0)
    }

    @Test
    fun `should return 401 Unauthorized for POST Cart with no token`() {
        RestAssured.given().log().all()
            .`when`().post("/api/cart")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 401 Unauthorized for POST Cart with an invalid USER token`() {
        RestAssured.given().log().all()
            .header("Authorization", "Bearer Token")
            .`when`().post("/api/cart")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 400 Bad Request for negative quantity`() {
        val product = createTestProduct("lotion", 20.0, 20)
        val requestBody = AddToCartRequest(product.id, -1)

        RestAssured.given().log().all()
            .header("Authorization", "Bearer $userToken")
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`().post("/api/cart")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `Should return 400 Bad Requests if the quantity exceeds product stock`() {
        val product = createTestProduct("lotion", 20.0, 2)
        val requestBody = AddToCartRequest(product.id, 3)

        RestAssured.given().log().all()
            .header("Authorization", "Bearer $userToken")
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`().post("/api/cart")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @DisplayName("Integration Tests for Removing Products from Cart (DELETE /api/cart/items/{productId})")
    @Test
    fun `Should return 204 No Content for a valid USER token`() {
        val product = createTestProduct("lotion", 20.0, 20)
        val userCart = createTestCart(userMember.userId)
        createTestCartItem(userCart.id, product.id, 3)

        RestAssured.given().log().all()
            .header("Authorization", "Bearer $userToken")
            .`when`().delete("/api/cart/{productId}", product.id)
            .then().log().all()
            .assertThat().statusCode(HttpStatus.NO_CONTENT.value())

        val remainingItem = cartItemRepository.findByCartId(userCart.id)
        assertThat(remainingItem).isEmpty()
    }

    @Test
    fun `Should return 401 Unauthorized for DELETE with no token`() {
        val product = createTestProduct("lotion", 20.0, 20)
        RestAssured.given().log().all()
            .`when`().delete("/api/cart/{productId}", product.id)
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 401 Unauthorized for DELETE with an invalid token`() {
        val product = createTestProduct("lotion", 20.0, 20)
        RestAssured.given().log().all()
            .header("Authorization", "Bearer Token")
            .`when`().delete("/api/cart/{productId}", product.id)
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 404 Not Found if product not in the cart`() {
        val product = createTestProduct("lotion", 20.0, 20)
        createTestCart(userMember.userId)

        RestAssured.given().log().all()
            .header("Authorization", "Bearer $userToken")
            .`when`().delete("/api/cart/{productId}", product.id)
            .then().log().all()
            .assertThat().statusCode(HttpStatus.NOT_FOUND.value())
    }
}
