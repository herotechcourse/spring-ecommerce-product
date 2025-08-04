package ecommerce.controller

import ecommerce.auth.JwtProvider
import ecommerce.model.Member
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberControllerTest {
    @Test
    fun `register() be able return 'created 201' response`() {
        val requestBody =
            mapOf(
                "email" to "memberTeste@test.com",
                "password" to "12345",
                "name" to "Gabi",
                "role" to "USER",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .`when`().post("/api/members")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `register() return '400' response if request body is not correctly filled`() {
        val requestBody =
            mapOf(
                "password" to "12345",
                "name" to "Test",
                "role" to "USER",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .`when`().post("/api/members")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `register() return '400' response if try to register duplicate email`() {
        val requestBody =
            mapOf(
                "email" to "admin@test.com",
                "password" to "12345",
                "name" to "Gabi",
                "role" to "USER",
            )
        val response =
            RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .`when`().post("/api/members")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `get() should be able to get a member and return 'ok 200' response`() {
        val token = JwtProvider.generateToken("admin@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().get("/api/members/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())

        val member = response.`as`(Member::class.java)
        assertThat(member.id).isEqualTo(1L)
    }

    @Test
    fun `get() should return '401' response when user is not authorized with a token`() {
        val token = JwtProvider.generateToken("invalid@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().get("/api/members/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `get() should return '404' response when user tries to get another member`() {
        val token = JwtProvider.generateToken("user@test.com")

        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().get("/api/members/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
