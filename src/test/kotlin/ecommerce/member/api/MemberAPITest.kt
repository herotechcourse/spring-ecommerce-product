package ecommerce.member.api

import ecommerce.member.data.TokenResponse
import ecommerce.member.helper.RequestExecutor
import ecommerce.member.helper.TestFixture.PostMembersRegister
import ecommerce.member.helper.CustomAssertExtension.shouldEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class MemberAPITest {
    @Test
    fun `should register admin and return expected token`() {
        val executor = RequestExecutor(PostMembersRegister.VALID_ADMIN)
        val actual = executor.execute()

        val token = actual.body().`as`(TokenResponse::class.java)
        assertThat(actual.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        token.shouldEquals(executor.expect)
    }
}
