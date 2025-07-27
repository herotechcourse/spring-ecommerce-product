package ecommerce.api.product

import ecommerce.products.model.Product
import ecommerce.products.model.ProductPatchDTO
import ecommerce.products.service.ProductService
import ecommerce.products.store.JdbcProductStore
import ecommerce.products.store.ProductStore
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
@Import(JdbcProductStore::class)
class ProductServiceTest {
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var productStore: ProductStore

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE;")

        jdbcTemplate.execute("DROP TABLE IF EXISTS cart_item;")
        jdbcTemplate.execute("DROP TABLE IF EXISTS cart;")
        jdbcTemplate.execute("DROP TABLE IF EXISTS member;")
        jdbcTemplate.execute("DROP TABLE IF EXISTS product;")

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE;")

        jdbcTemplate.execute(
            """
            CREATE TABLE product (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                price DOUBLE NOT NULL,
                imageUrl TEXT NOT NULL
            );
            """.trimIndent(),
        )

        val insertSql = "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)"
        jdbcTemplate.update(insertSql, "Iron Man", 1000.0, "https://alexnsan.comics/imageurl/1")
        jdbcTemplate.update(insertSql, "X-men", 1000.0, "https://alexnsan.comics/imageurl/2")
        jdbcTemplate.update(insertSql, "Superman", 1000.0, "https://alexnsan.comics/imageurl/3")
        jdbcTemplate.update(insertSql, "Naruto", 1000.0, "https://alexnsan.comics/imageurl/4")
        jdbcTemplate.update(insertSql, "Full Metal Alchemist", 1000.0, "https://alexnsan.comics/imageurl/5")

        productService = ProductService(productStore)
    }

    @Test
    fun findAll() {
        val products = productService.findAll()
        Assertions.assertThat(products).hasSize(5)
    }

    @Test
    fun findById() {
        val product = productService.findById(1)
        Assertions.assertThat(product?.name).isEqualTo("Iron Man")
    }

    @Test
    fun insert() {
        val product = Product(name = "Iron body", price = 99.0, imageUrl = "https://alexnsan.comics/imageurl/123")
        productService.insert(product)
        val allProducts = productService.findAll()
        val lastProduct = allProducts.last()
        Assertions.assertThat(lastProduct.name).isEqualTo(product.name)
    }

    @Test
    fun update() {
        val id = 1L
        val newProduct =
            ProductPatchDTO(name = "Iron body", price = 99.0, imageUrl = "https://alexnsan.comics/imageurl/123")

        productService.update(id, newProduct)

        val target = productService.findById(id)

        Assertions.assertThat(target?.id).isEqualTo(id)
        Assertions.assertThat(target?.name).isEqualTo(newProduct.name)
    }

    @Test
    fun delete() {
        val id = 1L
        val result = productService.delete(id)

        Assertions.assertThat(result).isTrue
        Assertions.assertThat(productService.findById(id)).isNull()
    }
}
