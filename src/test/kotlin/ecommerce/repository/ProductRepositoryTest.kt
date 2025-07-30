package ecommerce.repository

import ecommerce.domain.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
@Import(ProductRepository::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Product Repository Tests")
class ProductRepositoryTest
    @Autowired
    constructor(
        private val productRepository: ProductRepository,
        private val jdbcTemplate: JdbcTemplate,
    ) {
        @BeforeEach
        fun setUp() {
            jdbcTemplate.execute("delete from products")
        }

        @Test
        fun `it should count the products`() {
            val productLotion = productRepository.create(Product(name = "lotion", price = 20.0, img = "https://lotion.com", quantity = 20))
            val productSpf = productRepository.create(Product(name = "spf", price = 20.0, img = "https://spf.com", quantity = 20))
            val productCream = productRepository.create(Product(name = "cream", price = 20.0, img = "https://cream.com", quantity = 20))

            assertThat(productRepository.count()).isEqualTo(3)
            assertThat(productLotion.id).isGreaterThan(0)
            assertThat(productSpf.id).isGreaterThan(productLotion.id)
            assertThat(productCream.id).isGreaterThan(productSpf.id)
        }

        @Test
        fun `it should find a product by id`() {
            val createdProduct =
                productRepository.create(Product(name = "lotion", price = 20.0, img = "https://lotion.com", quantity = 20))
            assertThat(productRepository.findById(createdProduct.id)).isNotNull()
            assertThat(productRepository.findById(createdProduct.id)).isEqualTo(createdProduct)
        }

        @Test
        fun `it should return all products`() {
            val productLotion = productRepository.create(Product(name = "lotion", price = 20.0, img = "https://lotion.com", quantity = 20))
            val productSpf = productRepository.create(Product(name = "spf", price = 20.0, img = "https://spf.com", quantity = 20))
            val productCream = productRepository.create(Product(name = "cream", price = 20.0, img = "https://cream.com", quantity = 20))

            assertThat(productRepository.findAllProducts()).isEqualTo(listOf(productLotion, productSpf, productCream).toList())
        }

        @Test
        fun `it should update the product properties`() {
            val createdProduct = productRepository.create(Product(name = "lotion", price = 20.0, img = "https://lotion.com", quantity = 20))
            val newUpdatedProduct = createdProduct.copy(name = "spf", price = 30.0, img = "https://spf.com", quantity = 25)
            productRepository.update(createdProduct.id, product = newUpdatedProduct)
            assertThat(productRepository.findById(createdProduct.id)).isEqualTo(newUpdatedProduct)
            assertThat(productRepository.findById(createdProduct.id)?.img).isEqualTo(newUpdatedProduct.img)
        }

        @Test
        fun `it should delete a product by it's id`() {
            val createdProduct = productRepository.create(Product(name = "cream", price = 20.0, img = "https://cream.com", quantity = 20))
            productRepository.delete(createdProduct.id)
            assertThat(productRepository.count()).isEqualTo(0)
            assertThat(productRepository.findById(1)).isNull()
        }
    }
