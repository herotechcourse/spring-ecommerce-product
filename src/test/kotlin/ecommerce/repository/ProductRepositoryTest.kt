package ecommerce.repository

import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.domain.AbstractPersistable_.id

@JdbcTest
// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(ProductRepository::class)
class ProductRepositoryTest
    @Autowired
    constructor(
        private val productRepository: ProductRepository,
    ) {
        @Test
        fun `it should count the products`() {
            productRepository.create(
                Product(
                    id = 10001,
                    "Brush",
                    10.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSn2oLfY5lyGWYclR5fOrDkXxGHU-xgy7rPTQ&s",
                    10,
                ),
            )
            productRepository.create(
                Product(
                    id = 20001,
                    "Paint",
                    11.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8adusJSXz4emWhIh-gReKrPxjSW4xERp3Hw&s",
                    15,
                ),
            )
            productRepository.create(
                Product(
                    id = 30001,
                    "Pencils",
                    4.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQz7yiOlwdwC7Tojs4ziJRxbo_iqXRQCbc2g&s",
                    20,
                ),
            )

            assertThat(productRepository.count()).isEqualTo(6)
        }

        @Test
        fun `it should find a product by id`() {
            productRepository.create(Product(id = 4001, "findProduct", 20.0, "http://astr", 20))
            assertThat(productRepository.findById(4)).isNotNull()
            assertThat(productRepository.findById(4)).isEqualTo(Product(id = 4, "findProduct", 20.0, "http://astr", 20))
        }

        @Test
        fun `it should return all products`() {
            assertThat(productRepository.findAllProducts()).isEqualTo(
                listOf(
                    Product(
                        id = 1,
                        "Lotion",
                        10.0,
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSn2oLfY5lyGWYclR5fOrDkXxGHU-xgy7rPTQ&s",
                        10,
                    ),
                    Product(
                        id = 2,
                        "Cream",
                        11.0,
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8adusJSXz4emWhIh-gReKrPxjSW4xERp3Hw&s",
                        15,
                    ),
                    Product(
                        id = 3,
                        "Lip balm",
                        4.0,
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQz7yiOlwdwC7Tojs4ziJRxbo_iqXRQCbc2g&s",
                        20,
                    ),
                ).toList(),
            )
        }

        @Test
        fun `it should delete a product by it's id`() {
            productRepository.create(Product(id = 6001, "test", 20.0, "http://astr", 20))
            productRepository.delete(id = 6001)
            assertThat(productRepository.findById(6001)).isNull()
        }
    }
