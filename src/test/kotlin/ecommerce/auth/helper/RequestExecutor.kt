package ecommerce.auth.helper

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

class RequestExecutor<Req, Res>(
    private val helper: ApiTestHelper<Req, Res>,
) {
    val expect: Res = helper.expectedBody

    fun execute(): ExtractableResponse<Response> {
        val given = buildRequest()
        val actual =
            given
                .`when`()
                .request(helper.httpRequestLine.method, helper.httpRequestLine.url)
                .then()
                .log().all()
                .extract()

        return actual
    }

    private fun buildRequest(): RequestSpecification {
        return RestAssured
            .given()
            .log().all()
            .apply {
                if (helper.httpRequestLine.method != "GET") {
                    body(helper.requestBody)
                    contentType(ContentType.JSON)
                }
            }
    }
}
