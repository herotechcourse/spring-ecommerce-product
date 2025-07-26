package ecommerce

import ecommerce.model.Member
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

object MemberMock {
    val MEMBER_1 = Member(
        id = 1,
        email = "member1@email.com",
        password = "1234"
    )
    val MEMBER_2 = Member(
        id = 2,
        email = "member2@email.com",
        password = "12345"
    )

    fun createMember(member: Member) {
        val response =
            RestAssured
                .given().log().all()
                .body(member)
                .contentType(ContentType.JSON)
                .`when`().post("/api/members")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }
}
