package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.exception.DuplicateProductNameException
import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ProductServiceTest {
    private lateinit var repository: ProductRepository
    private lateinit var service: ProductService

    @BeforeEach
    fun setUp() {
        repository = mockk()
        service = ProductService(repository)
    }

    @Test
    fun `should throw DuplicateProductNameException when product name exists`() {
        val request = ProductRequest(name = "Vanilla", price = 10.0, imageUrl = "https://image.jpg")
        every { repository.existsByName(any()) } returns true

        assertThrows<DuplicateProductNameException> {
            service.validateUniqueName(request)
        }
    }

    @Test
    fun `should pass silently when product name is unique`() {
        val request = ProductRequest(name = "Unique", price = 10.0, imageUrl = "https://image.jpg")
        every { repository.existsByName(any()) } returns false

        assertThatCode {
            service.validateUniqueName(request)
        }.doesNotThrowAnyException()
    }

    @Test
    fun `should create product and return id`() {
        val request = ProductRequest(name = "Choco", price = 5.0, imageUrl = "https://img.com")
        every { repository.create(any()) } returns 99L

        val id = service.create(request)

        assertEquals(99L, id)
    }

    @Test
    fun `should throw exception when create returns null`() {
        val request = ProductRequest(name = "Error", price = 5.0, imageUrl = "https://img.com")
        every { repository.create(any()) } returns null

        assertThrows<RuntimeException> {
            service.create(request)
        }
    }

    @Test
    fun `should return all products`() {
        val products =
            listOf(
                Product(name = "A", price = 1.0, imageUrl = "https://img.com/a.jpg", id = 1),
                Product(name = "B", price = 2.0, imageUrl = "https://img.com/b.jpg", id = 2),
            )
        every { repository.getAll() } returns products

        val result = service.getAll()

        assertEquals(products, result)
    }

    @Test
    fun `should update product if it exists`() {
        val request = ProductRequest(name = "Updated", price = 10.0, imageUrl = "https://img.com/img.jpg")
        every { repository.update(5L, any()) } returns true

        service.update(5L, request)

        verify { repository.update(5L, any()) }
    }

    @Test
    fun `should throw NotFoundException when updating non-existent product`() {
        val request = ProductRequest(name = "Updated", price = 10.0, imageUrl = "https://img.com/img.jpg")
        every { repository.update(5L, any()) } returns false

        assertThrows<NotFoundException> {
            service.update(5L, request)
        }
    }

    @Test
    fun `should delete product if it exists`() {
        every { repository.delete(7L) } returns true

        service.delete(7L)

        verify { repository.delete(7L) }
    }

    @Test
    fun `should throw NotFoundException when product does not exist during delete`() {
        every { repository.delete(8L) } returns false

        assertThrows<NotFoundException> {
            service.delete(8L)
        }
    }
}
