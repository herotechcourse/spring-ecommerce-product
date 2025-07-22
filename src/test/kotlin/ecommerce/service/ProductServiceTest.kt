package ecommerce.service

import ecommerce.dto.ProductDTO
import ecommerce.dto.ProductPatchDTO
import ecommerce.exception.DuplicateProductNameException
import ecommerce.exception.EntityNotFoundException
import ecommerce.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductServiceTest {
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var productService: ProductService

    @Test
    fun getAllProducts() {
        val products = productService.getAllProducts()
        assertThat(products).isNotEmpty
    }

    @Test
    fun getProductById() {
        val id = createProduct("getProductById")
        val product = productService.getProductById(id)
        assertThat(product.id).isEqualTo(id)
    }

    @Test
    fun `throws error if no product found getProductById`() {
        assertThrows<EntityNotFoundException> { productService.getProductById(-3) }
    }

    @Test
    fun createProduct() {
        val uri =
            productService.createProduct(
                ProductDTO(name = "Product1", description = "description", price = 10.5, imageUrl = "url.com"),
            )
        assertThat(uri).isNotNull
    }

    @Test
    fun `throws error if duplicate name createProduct`() {
        createProduct("createProduct")
        assertThrows<DuplicateProductNameException> {
            productService.createProduct(
                ProductDTO(name = "createProduct", description = "description", price = 10.5, imageUrl = "url.com"),
            )
        }
    }

    @Test
    fun updateProduct() {
        val id = createProduct("updateProduct")
        productService.updateProduct(
            id = id,
            product = ProductDTO(name = "updateProduct2", description = "description", price = 10.5, imageUrl = "url.com"),
        )
        val product = productService.getProductById(id)
        assertThat(product.name).isEqualTo("updateProduct2")
    }

    @Test
    fun `throws error if no product found updateProduct`() {
        assertThrows<EntityNotFoundException> {
            productService.updateProduct(
                -1,
                ProductDTO(name = "Product1", description = "description", price = 10.5, imageUrl = "url.com"),
            )
        }
    }

    @Test
    fun patchProduct() {
        val id = createProduct("patchProduct")
        productService.patchProduct(id, ProductPatchDTO(name = "Patched"))
        val product = productService.getProductById(id)
        assertThat(product.name).isEqualTo("Patched")
    }

    @Test
    fun `throws error if no product found patchProduct`() {
        assertThrows<EntityNotFoundException> {
            productService.patchProduct(
                -1,
                ProductPatchDTO(name = "Something"),
            )
        }
    }

    @Test
    fun deleteProduct() {
        val id = createProduct("deleteProduct")
        productService.deleteProduct(id)
        assertThrows<EntityNotFoundException> { productService.getProductById(id) }
    }

    @Test
    fun `Throws error if product not found deleteProduct`() {
        assertThrows<EntityNotFoundException> { productService.deleteProduct(-3) }
    }

    private fun createProduct(name: String): Long {
        return productRepository.create(
            ProductDTO(name = name, description = "description", price = 10.5, imageUrl = "url.com"),
        )
    }
}
