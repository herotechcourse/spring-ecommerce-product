package ecommerce.repository

import ecommerce.model.Product
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class JdbcProductRepositoryTest {
    private lateinit var jdbcProductStore: ProductRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        jdbcProductStore = JdbcProductRepository(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE IF EXISTS cart_items")
        jdbcTemplate.execute("DROP TABLE IF EXISTS products")
        jdbcTemplate.execute(
            "CREATE TABLE products(" +
                "id BIGINT AUTO_INCREMENT, name VARCHAR(255) NOT NULL, price DOUBLE NOT NULL, image_url VARCHAR(512) NOT NULL)",
        )

        val products =
            listOf(
                Product(
                    id = 1L,
                    name = "vanilla",
                    price = 1.99,
                    imageUrl = "https://laurenslatest.com/wp-content/uploads/2020/08/vanilla-ice-cream-5-copy-360x361.jpg",
                ),
                Product(
                    id = 2L,
                    name = "pistachio",
                    price = 2.49,
                    imageUrl = "https://greenhealthycooking.com/wp-content/uploads/2017/06/Pistachio-Ice-Cream-Photo.jpg",
                ),
                Product(
                    id = 3L,
                    name = "chocolate",
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
        val products = jdbcProductStore.getAll()
        Assertions.assertThat(products.size).isEqualTo(3)
    }

    @Test
    fun create() {
        val product =
            Product(
                name = "Carotte",
                price = 1.00,
                imageUrl = "https://farm8.staticflickr.com/7116/7618319284_7a441773e2_z.jpg",
            )
        jdbcProductStore.create(product)

        val products = jdbcProductStore.getAll()

        Assertions.assertThat(products.any { it.name == "Carotte" }).isTrue()
    }

    @Test
    fun update() {
        val product =
            Product(
                name = "Carotte",
                price = 1.00,
                imageUrl = "https://farm8.staticflickr.com/7116/7618319284_7a441773e2_z.jpg",
            )
        jdbcProductStore.update(1, product)

        val products = jdbcProductStore.getAll()

        Assertions.assertThat(products.first().name).isEqualTo(product.name)
        Assertions.assertThat(products.any { it.name == "Vanilla" }).isFalse()
    }

    @Test
    fun delete() {
        jdbcProductStore.delete(1)

        val products = jdbcProductStore.getAll()

        Assertions.assertThat(products.size).isEqualTo(2)
        Assertions.assertThat(products.any { it.name == "Vanilla" }).isFalse()
    }
}
