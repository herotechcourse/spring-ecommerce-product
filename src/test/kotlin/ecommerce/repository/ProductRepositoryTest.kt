package ecommerce.repository

import ecommerce.entity.Product
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class ProductRepositoryTest {
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepository(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE cart_items IF EXISTS")
        jdbcTemplate.execute("DROP TABLE cart_item_history IF EXISTS")
        jdbcTemplate.execute("DROP TABLE products IF EXISTS")
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS products(" +
                "id BIGINT AUTO_INCREMENT, name VARCHAR(255) NOT NULL, price DOUBLE NOT NULL, image_url VARCHAR(512) NOT NULL)",
        )

        val products =
            listOf(
                Product(
                    id = 1L,
                    name = "vanilla ice cream",
                    price = 1.99,
                    imageUrl = "https://laurenslatest.com/wp-content/uploads/2020/08/vanilla-ice-cream-5-copy-360x361.jpg",
                ),
                Product(
                    id = 2L,
                    name = "pistachio ice cream",
                    price = 2.49,
                    imageUrl = "https://greenhealthycooking.com/wp-content/uploads/2017/06/Pistachio-Ice-Cream-Photo.jpg",
                ),
                Product(
                    id = 3L,
                    name = "chocolate ice cream",
                    price = 1.49,
                    imageUrl = "https://www.cravethegood.com/wp-content/uploads/2021/04/sous-vide-chocolate-ice-cream-15.jpg",
                ),
            )

        jdbcTemplate.batchUpdate(
            "INSERT INTO products(name, price, image_url) VALUES (?, ?, ?)",
            products,
            products.size,
        ) { ps, product ->
            ps.setString(1, product.name)
            ps.setDouble(2, product.price)
            ps.setString(3, product.imageUrl)
        }
    }

    @Test
    fun getAll() {
        val products = productRepository.getAll()
        Assertions.assertThat(products.size).isEqualTo(3)
    }

    @Test
    fun create() {
        val product =
            Product(name = "Carotte ice cream", price = 1.00, imageUrl = "https://farm8.staticflickr.com/7116/7618319284_7a441773e2_z.jpg")
        productRepository.create(product)

        val products = productRepository.getAll()

        Assertions.assertThat(products.any { it.name == "Carotte ice cream" }).isTrue()
    }

    @Test
    fun update() {
        val product =
            Product(name = "Carotte ice cream", price = 1.00, imageUrl = "https://farm8.staticflickr.com/7116/7618319284_7a441773e2_z.jpg")
        productRepository.update(1, product)

        val products = productRepository.getAll()

        Assertions.assertThat(products.first().name).isEqualTo(product.name)
        Assertions.assertThat(products.any { it.name == "Vanilla ice cream" }).isFalse()
    }

    @Test
    fun delete() {
        productRepository.delete(1)

        val products = productRepository.getAll()

        Assertions.assertThat(products.size).isEqualTo(2)
        Assertions.assertThat(products.any { it.name == "Vanilla ice cream" }).isFalse()
    }
}
