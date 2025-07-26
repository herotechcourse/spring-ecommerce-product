package ecommerce.controller

import ecommerce.dto.TokenResponse
import ecommerce.helper.MemberTestFixture.RequestCases
import ecommerce.helper.MemberTestFixture.ValidationCase
import ecommerce.helper.MemberTestFixture.registerMember
import ecommerce.helper.TestExpected
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberControllerTest {
    @Test
    fun `should register admin and return expected token`() {
        val request = RequestCases.VALID_ADMIN
        val expect = TestExpected(request, ValidationCase.DEFAULT_CASE)

        val response =
            RestAssured
                .given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/register")
                .then().log().all().extract()

        val actual = response.body().`as`(TokenResponse::class.java)
        val actualClaims = TestExpected.decode(actual.accessToken, ValidationCase.DEFAULT_CASE)

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(actualClaims.subject).isEqualTo(expect.claims.subject)
        assertThat(actualClaims["email"]).isEqualTo(expect.claims["email"])
        assertThat(actualClaims["password"]).isNull()
    }

    @Test
    fun `should not allow duplicate email registration`() {
        val request = RequestCases.VALID_ADMIN

        RestAssured.given().body(request)
            .contentType(ContentType.JSON)
            .post("/api/members/register")

        val response =
            RestAssured.given().body(request)
                .contentType(ContentType.JSON)
                .post("/api/members/register")

        assertThat(response.statusCode).isEqualTo(HttpStatus.CONFLICT.value())
    }

    @Test
    fun `should login and return expected token`() {
        val request = RequestCases.VALID_ADMIN
        registerMember(request)

        val expect = TestExpected(request, ValidationCase.DEFAULT_CASE)

        val response =
            RestAssured
                .given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/login")
                .then().log().all().extract()

        val actual = response.body().`as`(TokenResponse::class.java)
        val actualClaims = TestExpected.decode(actual.accessToken, ValidationCase.DEFAULT_CASE)

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(actualClaims.subject).isEqualTo(expect.claims.subject)
        assertThat(actualClaims["email"]).isEqualTo(expect.claims["email"])
        assertThat(actualClaims["password"]).isNull()
    }

    @Test
    fun `should return 401 when logging in with unregistered email`() {
        val request = RequestCases.VALID_ADMIN
        val expect = TestExpected(request, ValidationCase.DEFAULT_CASE)

        val response =
            RestAssured
                .given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/login")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }
}
