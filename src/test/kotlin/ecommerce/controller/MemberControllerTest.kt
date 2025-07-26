package ecommerce.controller

import ecommerce.MemberMock.MEMBER_1
import ecommerce.MemberMock.createMember
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import ecommerce.model.Member

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberControllerTest {
    @Test
    fun `register() be able return 'created 201' response`() {
        val requestBody = mapOf(
            "email" to "memberTeste@test.com",
            "password" to "12345",
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
    fun `get() should be able to get a member and return 'ok 200' response`() {
        createMember(MEMBER_1)

        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/members/1")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.jsonPath().getList("", Member::class.java)).hasSize(1)
    }

}
