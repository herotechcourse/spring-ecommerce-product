package ecommerce.helper

import ecommerce.dto.MemberRequest
import ecommerce.entity.Member
import io.jsonwebtoken.Jwts
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response

object MemberTestFixture {
    object Cases {
        val VALID_REQUEST =
            MemberRequest(
                email = "admin@email.com",
                password = "password",
            )

        val VALID_REQUEST_GURI =
            MemberRequest(
                email = "guri@email.com",
                password = "very_cute_dog",
            )

        val MEMBER_GURI =
            Member(
                id = 1L,
                email = VALID_REQUEST_GURI.email,
                password = VALID_REQUEST_GURI.password,
            )
    }

    object ValidationCase {
        val DEFAULT_CASE =
            JwtTestSetup(
                secretKey = "TGZtZ3VyaU94UVZ1d2k2NGhNVmxuY3AzZ1JHVWltcE5HTnZkNWQzZg==",
                expireLength = 3600000,
                algorithm = Jwts.SIG.HS256,
            )
    }

    fun registerMember(
        request: MemberRequest,
        path: String = "/api/members/register",
    ): ExtractableResponse<Response> {
        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(ContentType.JSON)
            .`when`().post(path)
            .then().log().all().extract()
    }
}
