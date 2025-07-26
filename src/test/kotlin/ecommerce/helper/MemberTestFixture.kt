package ecommerce.helper

import ecommerce.dto.MemberRequest
import io.jsonwebtoken.Jwts
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response

object MemberTestFixture {
    object RequestCases {
        val VALID_ADMIN =
            MemberRequest(
                email = "admin@email.com",
                password = "password",
            )
    }

    object ValidationCase {
        val DEFAULT_CASE =
            ValidationTestSet(
                key = "TGZtZ3VyaU94UVZ1d2k2NGhNVmxuY3AzZ1JHVWltcE5HTnZkNWQzZg==",
                validityInMilliseconds = 3600000,
                algorithm = Jwts.SIG.HS256,
            )
    }

    fun registerMember(request: MemberRequest, path: String = "/api/members/register"): ExtractableResponse<Response> {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/members/register")
                .then().log().all().extract()
    }
}
