package ecommerce.service

import ecommerce.dto.ProductForm
import ecommerce.exception.ProductNameAlreadyExistsException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(
    scripts = ["/sql/product.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
)
class ProductServiceTest(
    @Autowired private val productService: ProductService,
) {
    @Test
    fun `insert() - should throw an exception when name of product already exists`() {
        val productForm = ProductForm(name = "Iron Man", price = 1.5, imageUrl = "https://www.product.com/image/1")
        assertThrows<ProductNameAlreadyExistsException> { productService.insert(productForm) }
    }

    @Test
    fun `insert() - should insert and return the product when name of product not exists`() {
        val productForm = ProductForm(name = "Iron Body", price = 1.5, imageUrl = "https://www.product.com/image/1")
        val result = productService.insert(productForm)
        assertThat(result.id).isNotNull()
        assertThat(result.name).isEqualTo(productForm.name)
        assertThat(result.price).isEqualTo(productForm.price)
        assertThat(result.imageUrl).isEqualTo(productForm.imageUrl)
    }

    @Test
    fun `update() - should throw an exception when name of product already exists`() {
        val id = 2L
        val productForm = ProductForm(name = "Iron Man", price = 10.5, imageUrl = "https://www.product.com/image/1")
        assertThrows<ProductNameAlreadyExistsException> { productService.update(productForm, id) }
    }

    @Test
    fun `update() - should update the product when original product and new product have same name`() {
        val id = 1L
        val productForm = ProductForm(name = "Iron Man", price = 10.5, imageUrl = "https://www.product.com/image/1")
        val response = productService.update(productForm, id)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(ProductForm.toEntity(productForm, id))
    }

    @Test
    fun `update() - should update the product when name of new product does not exists`() {
        val id = 1L
        val productForm = ProductForm(name = "Stone Body", price = 1.5, imageUrl = "https://www.product.com/image/1")
        val response = productService.update(productForm, id)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(ProductForm.toEntity(productForm, id))
    }
}
