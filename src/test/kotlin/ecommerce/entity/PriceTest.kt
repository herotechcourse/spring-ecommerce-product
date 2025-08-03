package ecommerce.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class PriceTest {
    @Test
    fun `Price throws if less than 0_01`() {
        val exception = assertThrows<IllegalArgumentException> {
            Price(0.0)
        }
        assertThat(exception.message).contains("Price must be greater than 0.")
    }
}
