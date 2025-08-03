package ecommerce.controller

import ecommerce.dto.ProductRequest
import ecommerce.model.Product
import ecommerce.service.ProductService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI
import kotlin.test.assertEquals

class ProductControllerTest {
    private val service: ProductService = mock(ProductService::class.java)
    private val controller = ProductController(service)

    @Test
    fun `should create product and return 201 with Location header`() {
        val request = ProductRequest(name = "Apple", price = 1.0, imageUrl = "https://example.com/image.jpg")
        `when`(service.create(request)).thenReturn(42L)

        val response: ResponseEntity<Unit> = controller.createProduct(request)

        verify(service).validateUniqueName(request)
        verify(service).create(request)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(URI.create("/api/products/42"), response.headers.location)
    }

    @Test
    fun `should return all products`() {
        val products =
            listOf(
                Product(id = 1, name = "Apple", price = 1.0, imageUrl = "https://img.com/a.jpg"),
                Product(id = 2, name = "Banana", price = 2.0, imageUrl = "https://img.com/b.jpg"),
            )
        `when`(service.getAll()).thenReturn(products)

        val response = controller.readProducts()

        verify(service).getAll()
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(products, response.body)
    }

    @Test
    fun `should update a product and return 200`() {
        val request = ProductRequest(name = "Updated", price = 2.5, imageUrl = "https://img.com/updated.jpg")

        val response = controller.updateProduct(request, id = 5L)

        verify(service).update(5L, request)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `should delete a product and return 204`() {
        val response = controller.deleteProduct(id = 7L)

        verify(service).delete(7L)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}
