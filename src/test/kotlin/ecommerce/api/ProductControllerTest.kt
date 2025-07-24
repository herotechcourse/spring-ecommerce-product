package ecommerce.api

import ecommerce.dao.JdbcProductDAO
import ecommerce.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@JdbcTest
class ProductControllerTest {
    private lateinit var jdbcProductDao: JdbcProductDAO

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var controller: ProductController

    @BeforeEach
    fun setUp() {
        jdbcProductDao = JdbcProductDAO(jdbcTemplate)

        jdbcTemplate.execute("DROP TABLE product CASCADE")
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

        controller = ProductController(jdbcProductDao)
    }

    @Test
    fun create() {
        val product = Product(name = "product1", price = 1.5, imageUrl = "https://www.product.com/image/1")
        val response = controller.createProduct(product)

        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun readProducts() {
        create()
        create()
        val response = controller.getProducts()
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun readProduct() {
        create()
        create()
        val response = controller.getProduct(2)
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `readProduct() - but product doesn't exist`() {
        create()
        create()
        val response = controller.getProduct(10)
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun update() {
        val newProduct = Product(name = "new product", price = 1.6, imageUrl = "https://www.product.com/image/2")
        create()
        val response = controller.updateProduct(1, newProduct)
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
        val actual = response.body
        assertThat(actual?.name).isEqualTo(newProduct.name)
        assertThat(actual?.price).isEqualTo(newProduct.price)
        assertThat(actual?.imageUrl).isEqualTo(newProduct.imageUrl)
    }

    @Test
    fun delete() {
        create()
        val response = controller.deleteProduct(1)
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.NO_CONTENT.value())
        readProducts()
    }
}
