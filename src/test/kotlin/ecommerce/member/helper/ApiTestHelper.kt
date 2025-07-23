package ecommerce.member.helper

data class ApiTestHelper<Req, Res>(
    val httpRequestLine: RequestLine,
    val requestBody: Req,
    val expectedBody: Res,
)
