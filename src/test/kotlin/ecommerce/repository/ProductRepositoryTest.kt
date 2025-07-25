package ecommerce.repository

import ecommerce.domain.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
@Import(ProductRepository::class)
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
            val p1 = productRepository.create(Product(name = "test", price = 20.0, img = "http://astr", quantity = 20))
            val p2 = productRepository.create(Product(name = "test2", price = 20.0, img = "http://astr", quantity = 20))
            val p3 = productRepository.create(Product(name = "test3", price = 20.0, img = "http://astr", quantity = 20))

            assertThat(productRepository.count()).isEqualTo(3)
            assertThat(p1.id).isGreaterThan(0)
            assertThat(p2.id).isGreaterThan(p1.id)
            assertThat(p3.id).isGreaterThan(p2.id)
        }

        @Test
        fun `it should find a product by id`() {
            val createdProduct =
                productRepository.create(Product(name = "test", price = 20.0, img = "http://astr", quantity = 20))
            assertThat(productRepository.findById(createdProduct.id)).isNotNull()
            assertThat(productRepository.findById(createdProduct.id)).isEqualTo(createdProduct)
        }

        @Test
        fun `it should return all products`() {
            val p1 = productRepository.create(Product(name = "test", price = 20.0, img = "http://astr", quantity = 20))
            val p2 = productRepository.create(Product(name = "test2", price = 20.0, img = "http://astr", quantity = 20))
            val p3 = productRepository.create(Product(name = "test3", price = 20.0, img = "http://astr", quantity = 20))

            assertThat(productRepository.findAllProducts()).isEqualTo(listOf(p1, p2, p3).toList())
        }

        @Test
        fun `it should update the product properties`() {
            val createdProduct = productRepository.create(Product(name = "test", price = 20.0, img = "http://astr", quantity = 20))
            val newUpdatedProduct = createdProduct.copy(name = "test2", price = 30.0, img = "http://astrooooo", quantity = 25)
            productRepository.update(createdProduct.id, product = newUpdatedProduct)
            assertThat(productRepository.findById(createdProduct.id)).isEqualTo(newUpdatedProduct)
            assertThat(productRepository.findById(createdProduct.id)?.img).isEqualTo(newUpdatedProduct.img)
        }

        @Test
        fun `it should delete a product by it's id`() {
            val createdProduct = productRepository.create(Product(name = "test", price = 20.0, img = "http://astr", quantity = 20))
            productRepository.delete(createdProduct.id)
            assertThat(productRepository.count()).isEqualTo(0)
            assertThat(productRepository.findById(1)).isNull()
        }
    }
