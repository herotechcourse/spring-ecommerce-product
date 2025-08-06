package ecommerce.controller

import com.fasterxml.jackson.databind.ObjectMapper
import ecommerce.domain.NewProduct
import ecommerce.exception.NotFoundException
import ecommerce.helper.ProductRequestCases
import ecommerce.helper.ProductTestExpected
import ecommerce.service.AuthService
import ecommerce.service.ProductService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argForWhich
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

@WebMvcTest(ProductController::class)
class ProductControllerTest
    @Autowired
    constructor(
        private val mockMvc: MockMvc,
    ) {
        @MockitoBean
        private lateinit var productService: ProductService

        @MockitoBean
        private lateinit var authService: AuthService

        @Autowired
        private lateinit var objectMapper: ObjectMapper

        @Test
        fun `should create product and return 201 Created`() {
            val request = ProductRequestCases.AMERICANO
            val expect = ProductTestExpected(request)
            val expectedProduct = expect.product

            whenever(productService.insertNewProduct(any<NewProduct>()))
                .thenReturn(expectedProduct)

            mockMvc.post("/api/products") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect {
                    status { isCreated() }
                    jsonPath("$.id") { value(expect.id) }
                    jsonPath("$.name") { value(request.name) }
                    jsonPath("$.price") { value(request.price) }
                    jsonPath("$.imageUrl") { value(request.imageUrl) }
                }

            verify(productService).insertNewProduct(
                argForWhich {
                    name == request.name && price == request.price && imageUrl == request.imageUrl
                },
            )
        }

        @Test
        fun `should return 400 Bad Request when imageUrl exceeds max length`() {
            val request = ProductRequestCases.InvalidRequest.INVALID_IMAGE_URL_EXCEED

            mockMvc.post("/api/products") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        fun `should return 400 Bad Request when name is blank`() {
            val request = ProductRequestCases.InvalidRequest.INVALID_NAME_BLANKS

            mockMvc.post("/api/products") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        fun `should return 400 Bad Request when price is too small`() {
            val request = ProductRequestCases.InvalidRequest.INVALID_PRICE_TOO_SMALL

            mockMvc.post("/api/products") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        fun `should return 400 Bad Request when imageUrl is blank`() {
            val request = ProductRequestCases.InvalidRequest.INVALID_IMAGE_URL_BLANKS

            mockMvc.post("/api/products") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        fun `should delete product and return 204 No Content`() {
            val productId = 1L

            doNothing().whenever(productService).deleteProductById(productId)

            mockMvc.delete("/api/products/{id}", productId)
                .andExpect {
                    status { isNoContent() }
                }

            verify(productService).deleteProductById(productId)
        }

        @Test
        fun `should return 404 Not Found when deleting non-existent product`() {
            val productId = 0L

            doThrow(NotFoundException("Product with id $productId not found"))
                .whenever(productService).deleteProductById(productId)

            mockMvc.delete("/api/products/{id}", productId)
                .andExpect {
                    status { isNotFound() }
                }

            verify(productService).deleteProductById(productId)
        }
    }
