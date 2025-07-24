package ecommerce.auth.helper

import ecommerce.auth.data.MemberRequest
import ecommerce.auth.data.TokenResponse

object TestFixture {
    object PostMembersRegister {
        val VALID_ADMIN =
            ApiTestHelper(
                httpRequestLine = Headers.VALID_ADMIN_REGISTER,
                requestBody = TokenRequest.VALID_ADMIN_REGISTER,
                expectedBody = TokenResponse.VALID_ADMIN_REGISTER,
            )
    }

    private object Headers {
        val VALID_ADMIN_REGISTER = RequestLine("POST /api/members/register HTTP/1.1")
    }

    private object TokenRequest {
        val VALID_ADMIN_REGISTER =
            MemberRequest(
                email = "admin@email.com",
                password = "password",
            )
    }

    private object TokenResponse {
        val VALID_ADMIN_REGISTER =
            TokenResponse(
                accessToken = "",
            )
    }
}
