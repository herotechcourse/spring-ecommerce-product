package ecommerce.auth.controller

import ecommerce.auth.data.TokenResponse
import ecommerce.auth.helper.RequestExecutor
import ecommerce.auth.helper.TestFixture.PostMembersRegister
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class MemberControllerTest {
    @Test
    fun `should register admin and return expected token`() {
        val executor = RequestExecutor(PostMembersRegister.VALID_ADMIN)
        val actual = executor.execute()

        val token = actual.body().`as`(TokenResponse::class.java)
        assertThat(actual.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(token.accessToken).isEqualTo(executor.expect.accessToken)
    }
}
