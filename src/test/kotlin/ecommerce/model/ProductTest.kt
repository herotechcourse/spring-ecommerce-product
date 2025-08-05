package ecommerce.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.codehaus.groovy.runtime.DefaultGroovyMethods.toBigDecimal
import org.junit.jupiter.api.Test

class ProductTest {
    @Test
    fun `should throw illegal exception when name is blank`() {
        assertThatThrownBy {
            Product(
                id = 1L,
                name = "",
                price = toBigDecimal(10.00),
                imageUrl = "https://example.com/images/espresso-beans.jpg",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Name cannot be blank")
    }

    @Test
    fun `should throw illegal exception when name length is bigger then 255 characters`() {
        assertThatThrownBy {
            Product(
                id = 1L,
                name = "Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                price = toBigDecimal(10.00),
                imageUrl = "https://example.com/images/espresso-beans.jpg",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Name must be at most 255 characters")
    }

    @Test
    fun `should throw illegal exception when price is negative`() {
        assertThatThrownBy {
            Product(
                id = 1L,
                name = "Jon",
                price = toBigDecimal(-10.00),
                imageUrl = "https://example.com/images/espresso-beans.jpg",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Price must be positive")
    }

    @Test
    fun `should throw illegal exception when image length is bigger then 255 characters`() {
        assertThatThrownBy {
            Product(
                id = 1L,
                name = "Jon",
                price = toBigDecimal(-10.00),
                imageUrl = "https://example.com/images/espresso-beAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaans.jpg",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Price must be positive")
    }
}
