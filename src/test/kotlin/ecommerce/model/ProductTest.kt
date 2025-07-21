import ecommerce.model.Product
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductTest {
    @Test
    fun `should create product with valid data`() {
        val product =
            Product(
                id = 1,
                name = "Apple",
                price = 1.99,
                imageUrl = "http://image.com/apple.jpg",
            )
        assertEquals("Apple", product.name)
    }

    @Test
    fun `should throw exception for blank name`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Product(name = " ")
            }
        assertEquals("Product name must not be blank.", exception.message)
    }

    @Test
    fun `should throw exception for negative price`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Product(name = "Apple", price = -5.0)
            }
        assertEquals("Product price must be non-negative.", exception.message)
    }

    @Test
    fun `should throw exception for long name`() {
        val longName = "a".repeat(256)
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Product(name = longName)
            }
        assertEquals("Product name must be 255 characters or fewer.", exception.message)
    }

    @Test
    fun `should throw exception for long imageUrl`() {
        val longUrl = "http://".padEnd(256, 'a')
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                Product(name = "Apple", imageUrl = longUrl)
            }
        assertEquals("Image URL must be 255 characters or fewer.", exception.message)
    }
}
