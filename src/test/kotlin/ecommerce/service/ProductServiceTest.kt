package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.web.server.ResponseStatusException

class ProductServiceTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var productService: ProductService

    @BeforeEach
    fun setup() {
        productRepository = mock(ProductRepository::class.java)
        productService = ProductService(productRepository)
    }

    @Test
    fun `createProduct throws if name is longer than 15 characters`() {
        val longName = "ThisNameIsWayTooLong"
        val request = ProductRequest(name = longName, price = 1.0, imageUrl = "https://valid.url")

        val exception = assertThrows<ResponseStatusException> {
            productService.createProduct(request)
        }

        assertThat(exception.reason).contains("Name must be between 1 and 15 characters")
    }

    @Test
    fun `createProduct throws if name is invalid`() {
        val invalidName = "Invalid@Name!"
        val request = ProductRequest(name = invalidName, price = 1.99, imageUrl = "https://valid.url")

        val exception = assertThrows<ResponseStatusException> {
            productService.createProduct(request)
        }

        assertThat(exception.reason).contains("Name contains invalid special characters.")
    }

    @Test
    fun `createProduct throws if price is less than 0_01`() {
        val request = ProductRequest(name = "ValidName", price = 0.0, imageUrl = "https://valid.url")

        val exception = assertThrows<ResponseStatusException> {
            productService.createProduct(request)
        }

        assertThat(exception.reason).contains("Price must be greater than 0.")
    }

    @Test
    fun `createProduct throws if name is not unique`() {
        val name = "UniqueName"
        `when`(productRepository.existsByName(name)).thenReturn(true)

        val request = ProductRequest(name = name, price = 1.0, imageUrl = "https://valid.url")

        val exception = assertThrows<ResponseStatusException> {
            productService.createProduct(request)
        }

        assertThat(exception.reason).contains("Product name must be unique.")
    }

    @Test
    fun `createProduct throws if imageUrl does not start with http or https`() {
        val request = ProductRequest(
            name = "ValidName",
            price = 1.0,
            imageUrl = "ftp://invalid.url"
        )

        val exception = assertThrows<ResponseStatusException> {
            productService.createProduct(request)
        }

        assertThat(exception.reason).contains("Image URL must start with http:// or https://")
    }

    @Test
    fun `createProduct succeeds with valid data`() {
        val name = "ValidName"
        val price = 1.0
        val imageUrl = "https://valid.url"

        val request = ProductRequest(name = name, price = price, imageUrl = imageUrl)
        val expectedProduct = request.toEntity()

        `when`(productRepository.existsByName(name)).thenReturn(false)
        `when`(productRepository.create(expectedProduct)).thenReturn(1L)

        val result = productService.createProduct(request)

        assert(result == 1L)
        verify(productRepository).create(expectedProduct)
    }
}
