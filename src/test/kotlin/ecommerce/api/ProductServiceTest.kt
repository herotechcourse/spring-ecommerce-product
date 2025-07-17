package ecommerce.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class ProductServiceTest {
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        productService = ProductService(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE product IF EXISTS")
        jdbcTemplate.execute(
            """CREATE TABLE product (
                         id          LONG    NOT NULL AUTO_INCREMENT,
                         name        varchar(255)    NOT NULL,
                         price       DOUBLE  NOT NULL,
                         imageUrl    TEXT    NOT NULL,
                         PRIMARY KEY (id)
                    );""",
        )

        val query = """INSERT INTO product (name, price, imageUrl) VALUES ('Iron Man', 1000, 'https://alexnsan.comics/imageurl/1');
                    INSERT INTO product (name, price, imageUrl) VALUES ('X-men', 1000, 'https://alexnsan.comics/imageurl/2');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Superman', 1000, 'https://alexnsan.comics/imageurl/3');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Naruto', 1000, 'https://alexnsan.comics/imageurl/4');
                    INSERT INTO product (name, price, imageUrl) VALUES ('Full Metal Alchemist', 1000, 'https://alexnsan.comics/imageurl/5');"""
        jdbcTemplate.batchUpdate(query)
    }

    @Test
    fun findAll() {
        val products = productService.findAll()
        assertThat(products).hasSize(5)
    }

    @Test
    fun findById() {
        val product = productService.findById(1)
        assertThat(product?.name).isEqualTo("Iron Man")
    }

    @Test
    fun insert() {
        val product = Product(name = "Iron body", price = 99.0, imageUrl = "https://alexnsan.comics/imageurl/123")
        productService.insert(product)
        val target = productService.findById(6)
        assertThat(target?.name).isEqualTo(product.name)
    }

    @Test
    fun update() {
        val id = 1.toLong()
        val newProduct = Product(name = "Iron body", price = 99.0, imageUrl = "https://alexnsan.comics/imageurl/123")

        productService.update(id, newProduct)

        val target = productService.findById(id)

        assertThat(target?.id).isEqualTo(id)
        assertThat(target?.name).isEqualTo(newProduct.name)
    }

    @Test
    fun delete() {
        val id = 1.toLong()
        val result = productService.delete(id)

        assertThat(result).isEqualTo(1)
    }
}
