package ecommerce.repository

import ecommerce.domain.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext

@JdbcTest
@Import(ProductRepository::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductRepositoryTest
    @Autowired
    constructor(
        private val productRepository: ProductRepository,
    ) {
        @Test
        fun `it should count the products`() {
            productRepository.create(Product(id = 1, "test", 20.0, "http://astr", 20))
            productRepository.create(Product(id = 2, "test2", 20.0, "http://astr", 20))
            productRepository.create(Product(id = 3, "test3", 20.0, "http://astr", 20))

            assertThat(productRepository.count()).isEqualTo(3)
        }

        @Test
        fun `it should find a product by id`() {
            productRepository.create(Product(id = 1, "test", 20.0, "http://astr", 20))
            assertThat(productRepository.findById(1)).isNotNull()
            assertThat(productRepository.findById(1)).isEqualTo(Product(id = 1, "test", 20.0, "http://astr", 20))
        }

        @Test
        fun `it should return all products`() {
            productRepository.create(Product(id = 1, "test", 20.0, "http://astr", 20))
            productRepository.create(Product(id = 2, "test2", 20.0, "http://astr", 20))
            productRepository.create(Product(id = 3, "test3", 20.0, "http://astr", 20))

            assertThat(productRepository.findAllProducts()).isEqualTo(
                listOf(
                    Product(id = 1, "test", 20.0, "http://astr", 20),
                    Product(id = 2, "test2", 20.0, "http://astr", 20),
                    Product(id = 3, "test3", 20.0, "http://astr", 20),
                ).toList(),
            )
        }

        @Test
        fun `it should update the product properties`() {
            productRepository.create(Product(id = 1, "test", 20.0, "http://astr", 20))
            val newProduct = Product(id = 1, "test", 20.0, "http://IMG", 20)
            productRepository.update(id = 1, product = newProduct)
            assertThat(productRepository.findById(1)).isEqualTo(newProduct)
            assertThat(productRepository.findById(1)?.img).isEqualTo(newProduct.img)
        }

        @Test
        fun `it should delete a product by it's id`() {
            productRepository.create(Product(id = 1, "test", 20.0, "http://astr", 20))
            productRepository.delete(id = 1)
            assertThat(productRepository.count()).isEqualTo(0)
            assertThat(productRepository.findById(1)).isNull()
        }
    }
