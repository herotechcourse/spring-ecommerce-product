package ecommerce.controller.api

import ecommerce.controller.api.ProductController
import ecommerce.dao.JdbcProductDAO
import ecommerce.model.Product
import ecommerce.service.ProductService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired private lateinit var jdbcProductDao: JdbcProductDAO
    private lateinit var productService: ProductService
    private lateinit var controller: ProductController

    fun create() {
        val product = Product(name = "product1", price = 1.5, imageUrl = "https://www.product.com/image/1")
        jdbcProductDao.insert(product)
    }

    @BeforeEach
    fun setUp() {
        jdbcProductDao = JdbcProductDAO(jdbcTemplate)
        productService = ProductService(jdbcProductDao)

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

        controller = ProductController(productService)
    }

    @Test
    fun `create() - should insert product and return 201 when form is valid`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = "product1 [new]", price = 1.5, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun `create() - should return 400 when name is blank`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = "", price = 1.5, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val targets =
            listOf(
                "Contains unallowed character",
                "Product name is required",
                "Must be no more than 15 characters, including spaces",
            )
        val actual = response.jsonPath().getString("name")
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(targets)
    }

    @Test
    fun `create() - should return 400 when name is more than 15 characters`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = "this is very long name", price = 1.5, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val expected = "Must be no more than 15 characters, including spaces"
        val actual = response.jsonPath().getString("name")
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `create() - should return 400 when name has unallowed character`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = "I am product!", price = 1.5, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val expected = "Contains unallowed character"
        val actual = response.jsonPath().getString("name")
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `create() - should return 400 when price is 0`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = "base", price = 0.0, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val expected = "Product price must be greater than zero"
        val actual = response.jsonPath().getString("price")
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `create() - should return 400 when image URL is blank`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = "base", price = 2.0, imageUrl = ""))
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val expected =
            listOf(
                "Product image URL is required",
                "Must start with 'https://'.",
            )
        val actual = response.jsonPath().getString("imageUrl")
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isIn(expected)
    }

    @Test
    fun `create() - should return 400 when image URL is not valid`() {
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = "base", price = 2.0, imageUrl = "ssh://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val expected = "Must start with 'https://'."
        val actual = response.jsonPath().getString("imageUrl")
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `create() - should return 400 when name of product already exists`() {
        val name = "Iron Man"
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = name, price = 2.0, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val expected = "Product with name '$name' already exists."
        val actual = response.jsonPath().getString("name")
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual).isEqualTo(expected)
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
