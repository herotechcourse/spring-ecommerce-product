package ecommerce.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.codehaus.groovy.runtime.DefaultGroovyMethods.toBigDecimal
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProductTest {
    @Test
    fun `should throw illegal exception when name is blank`() {
        assertThatThrownBy {
            Product(
                id = 1L,
                name = "",
                price = BigDecimal.valueOf(10.00),
                imageUrl = "https://example.com/images/espresso-beans.jpg",
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Name cannot be blank")
    }

    @Test
    fun `should throw illegal exception when name length is bigger then 255 characters`() {
        val padding = "a".repeat(255)
        val longName = "${padding}d"

        assertThatThrownBy {
            Product(
                id = 1L,
                name = longName,
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
        val baseUrl = "https://example.com/"
        val padding = "a".repeat(240) // to make total length > 255
        val longUrl = baseUrl + padding + ".jpg" // safely over 255 characters

        println("Generated imageUrl length: ${longUrl.length}") // should be > 255
        assertThatThrownBy {
            Product(
                id = 1L,
                name = "Jon",
                price = toBigDecimal(10.00),
                imageUrl = longUrl,
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("URL address must be at most 255 characters")
    }
}
