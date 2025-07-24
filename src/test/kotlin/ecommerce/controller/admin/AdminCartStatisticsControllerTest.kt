package ecommerce.controller.admin

import ecommerce.dto.auth.LoginRequest
import ecommerce.dto.products.ProductDTO
import ecommerce.dto.products.ProductPatchDTO
import ecommerce.mapper.UserRowMapper
import ecommerce.repository.ProductRepository
import ecommerce.service.MemberAuthService
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
class AdminCartStatisticsControllerTest {
    private lateinit var token: String

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var userRowMapper: UserRowMapper

    @Autowired
    private lateinit var memberAuthService: MemberAuthService

    @BeforeEach
    fun init() {
        val sql = "select * from users where role = 'ADMIN'"
        val result = jdbcTemplate.query(sql, userRowMapper).first()
        token =
            memberAuthService.logIn(
                LoginRequest(
                    result.email, result.password,
                ),
            )
    }

    @Test
    fun adminStatisticsService() {
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/cart_statistics/top_products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun getMembersWhoAddedToCart() {
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/cart_statistics/members_added_cart")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }
}
