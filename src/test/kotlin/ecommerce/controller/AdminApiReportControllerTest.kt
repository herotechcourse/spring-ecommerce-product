package ecommerce.controller

import ecommerce.domain.CartEvent
import ecommerce.domain.Member
import ecommerce.domain.Product
import ecommerce.dto.member.AuthResponse
import ecommerce.dto.member.MemberLoginRequest
import ecommerce.repository.CartEventRepository
import ecommerce.repository.MemberRepository
import ecommerce.repository.ProductRepository
import ecommerce.repository.reportDto.MemberCartActivityDto
import ecommerce.repository.reportDto.ProductCartCountDto
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
@DisplayName("Admin Report Controller Integration Tests")
class AdminApiReportControllerTest {
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

    private val userEmail = "bo@gmail.com"
    private val userPassword = "MojataBebushkaAngie10%"
    private lateinit var userToken: String

    private val adminEmail = "admin@gmail.com"
    private val adminPassword = "AdminPasswordStrong10%"
    private lateinit var adminToken: String

    private fun registerAndLogin(
        userName: String,
        email: String,
        password: String,
        role: String,
    ): String {
        authService.registerMember(
            userName = userName,
            email = email,
            password = password,
            role = role,
        )

        val loginRequest = MemberLoginRequest(email = email, password = password)
        val loginResponse =
            RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .`when`().post("/api/members/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().`as`(AuthResponse::class.java)
        return loginResponse.token
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

    private fun createTestCartEvent(
        memberId: Long,
        productId: Long,
        quantity: Int,
        timestamp: LocalDateTime = LocalDateTime.now(),
    ): CartEvent {
        val event =
            CartEvent(
                memberId = memberId,
                productId = productId,
                quantityAdded = quantity,
                timestamp = timestamp,
            )
        return cartEventRepository.save(event)
    }

    private fun createTestMember(
        userName: String = "bo-test",
        email: String = "test-member-${System.nanoTime()}@example.com",
        password: String = "hashStrongPass%",
        role: String = "USER",
    ): Member {
        val member = Member(userName = userName, email = email, passwordHash = password, role = role)
        memberRepository.create(member)
        return member
    }

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        jdbcTemplate.execute("delete from cart_events")
        jdbcTemplate.execute("delete from cart_items")
        jdbcTemplate.execute("delete from carts")
        jdbcTemplate.execute("delete from products")
        jdbcTemplate.execute("delete from members")

        userToken = registerAndLogin("bo", userEmail, userPassword, "USER")
        adminToken = registerAndLogin("admin", adminEmail, adminPassword, "ADMIN")
    }

    @DisplayName("Integration Tests for Top 5 Most Added Products Report (GET /api/admin/reports/top-products?days=30)")
    @Test
    fun `Should return 200 OK and correct data with a valid ADMIN token`() {
        val adminMember = memberRepository.findByEmail(adminEmail)!!
        val product1 = createTestProduct("lotion", 20.0, 50)
        val product2 = createTestProduct("spf", 22.0, 100)
        val product3 = createTestProduct("cream", 18.0, 60)
        val product4 = createTestProduct("mask", 15.0, 75)
        val product5 = createTestProduct("shampoo", 10.0, 90)
        val product6 = createTestProduct("conditioner", 12.0, 40)
        val days = 30

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

        repeat(10) { createTestCartEvent(adminMember.userId, product1.id, 1, now.minusDays(1)) }
        repeat(8) { createTestCartEvent(adminMember.userId, product2.id, 1, now.minusDays(2)) }
        repeat(8) { createTestCartEvent(adminMember.userId, product3.id, 1, now.minusDays(3)) }
        repeat(7) { createTestCartEvent(adminMember.userId, product4.id, 1, now.minusDays(4)) }
        repeat(6) { createTestCartEvent(adminMember.userId, product5.id, 1, now.minusDays(5)) }
        repeat(5) { createTestCartEvent(adminMember.userId, product6.id, 1, now.minusDays(6)) }

        createTestCartEvent(adminMember.userId, product1.id, 1, now.minusDays(31))

        val response =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $adminToken")
                .`when`().get("/api/admin/reports/top-products?days=$days")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList("", ProductCartCountDto::class.java)

        assertThat(response).hasSize(5)
        assertThat(response[0].productName).isEqualTo(product1.name)
        assertThat(response[0].addedCount).isEqualTo(10)
        assertThat(response[1].productName).isEqualTo(product2.name)
        assertThat(response[1].addedCount).isEqualTo(8)
        assertThat(response[2].productName).isEqualTo(product3.name)
        assertThat(response[2].addedCount).isEqualTo(8)
        assertThat(response[3].productName).isEqualTo(product4.name)
        assertThat(response[3].addedCount).isEqualTo(7)
        assertThat(response[4].productName).isEqualTo(product5.name)
        assertThat(response[4].addedCount).isEqualTo(6)
    }

    @Test
    fun `Should return 403 Forbidden for GET top-products-30-days with a valid USER token`() {
        val days = 30
        RestAssured.given().log().all()
            .header("Authorization", "Bearer $userToken")
            .`when`().get("/api/admin/reports/top-products?days=$days")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun `Should return 401 Unauthorized for GET top-products-30-days with no token`() {
        val days = 30
        RestAssured.given().log().all()
            .`when`().get("/api/admin/reports/top-products?days=$days")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Should return 200 OK and an empty list if no relevant events exist`() {
        val days = 30
        val response =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $adminToken")
                .`when`().get("/api/admin/reports/top-products?days=$days")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList("", ProductCartCountDto::class.java)

        assertThat(response).isEmpty()
    }

    @DisplayName("Integration Tests for Members Who Added Items Report (GET /api/admin/reports/members-added-to-cart-7-days)")
    @Test
    fun `Should return 200 OK and correct unique member data with a valid ADMIN token`() {
        val member1 = createTestMember("bo")
        val member2 = createTestMember("elena")
        val member3 = createTestMember("ace")

        val product1 = createTestProduct("lotion", 10.0, 50)
        val product2 = createTestProduct("spf", 10.0, 70)

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val days = 7

        createTestCartEvent(member1.userId, product1.id, 1, now.minusDays(1))
        createTestCartEvent(member1.userId, product2.id, 1, now.minusDays(2))

        createTestCartEvent(member2.userId, product2.id, 1, now.minusDays(5))

        createTestCartEvent(member3.userId, product1.id, 1, now.minusDays(8))

        val expectedMembers =
            listOf(
                MemberCartActivityDto(userId = member1.userId, userName = member1.userName, email = member1.email),
                MemberCartActivityDto(userId = member2.userId, userName = member2.userName, email = member2.email),
            ).sortedBy { it.userId }

        val response =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer $adminToken")
                .`when`().get("/api/admin/reports/members-added-to-cart?days=$days")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList("", MemberCartActivityDto::class.java)

        assertThat(response).hasSize(expectedMembers.size)
        assertThat(response.sortedBy { it.userId }).isEqualTo(expectedMembers)
    }

    @Test
    fun `Should return 403 Forbidden for GET members-added-to-cart-7-days with a valid USER token`() {
        val days = 30
        RestAssured.given().log().all()
            .header("Authorization", "Bearer $userToken")
            .`when`().get("/api/admin/reports/top-products?days=$days")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun `Should return 401 Unauthorized for GET members-added-to-cart-7-days with no token`() {
        val days = 30
        RestAssured.given().log().all()
            .`when`().get("/api/admin/reports/top-products?days=$days")
            .then().log().all()
            .assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
    }
}
