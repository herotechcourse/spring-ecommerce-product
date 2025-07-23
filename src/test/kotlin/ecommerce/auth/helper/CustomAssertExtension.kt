package ecommerce.auth.helper

import ecommerce.auth.data.TokenResponse
import org.assertj.core.api.Assertions.assertThat

object CustomAssertExtension {
    fun TokenResponse.shouldEquals(expect: TokenResponse) {
        assertThat(this.accessToken).isEqualTo(expect.accessToken)
    }
}
