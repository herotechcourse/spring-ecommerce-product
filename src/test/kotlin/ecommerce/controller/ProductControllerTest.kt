package ecommerce.controller

import ecommerce.controller.api.ProductController
import ecommerce.dao.JdbcProductDAO
import ecommerce.dto.ProductForm
import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import ecommerce.service.ProductService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var jdbcProductDao: JdbcProductDAO
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

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
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
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["name"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isIn(targets)
    }

    @Test
    fun `create() - should return 400 when name is more than 15 characters`() {
        val response =
            RestAssured
                .given().log().all()
                .body(
                    Product(
                        name = "this is very long name",
                        price = 1.5,
                        imageUrl = "https://www.product.com/image/1",
                    ),
                )
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        val expected = "Must be no more than 15 characters, including spaces"
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["name"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isEqualTo(expected)
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
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["name"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isEqualTo(expected)
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
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["price"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isEqualTo(expected)
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
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["imageUrl"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isIn(expected)
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
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["imageUrl"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isEqualTo(expected)
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
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["name"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun readProducts() {
        create()
        create()
        val response = controller.getProducts()
        Assertions.assertThat(response.body?.size).isEqualTo(7)
        Assertions.assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun readProduct() {
        create()
        create()
        val response = controller.getProduct(2)
        Assertions.assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `readProduct() - but product doesn't exist`() {
        val id = 100
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().get("/api/products/$id")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `readProduct() - unit test, throw exception if product doesn't exist, `() {
        assertThrows<NotFoundException> { controller.getProduct(10) }
    }

    @Test
    fun update() {
        val targetId = 1L
        val newProductForm =
            ProductForm(name = "new product", price = 1.6, imageUrl = "https://www.product.com/image/2")
        create()
        val response = controller.updateProduct(targetId, newProductForm)
        Assertions.assertThat(response.statusCode.value()).isEqualTo(HttpStatus.OK.value())
        val actual = response.body
        Assertions.assertThat(actual?.id).isEqualTo(targetId)
        Assertions.assertThat(actual?.name).isEqualTo(newProductForm.name)
        Assertions.assertThat(actual?.price).isEqualTo(newProductForm.price)
        Assertions.assertThat(actual?.imageUrl).isEqualTo(newProductForm.imageUrl)
    }

    @Test
    fun `update() - should return 400 when name is blank`() {
        val targetId = 1L
        val name = ""
        val response =
            RestAssured
                .given().log().all()
                .body(Product(id = targetId, name = name, price = 1.5, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/$targetId")
                .then().log().all().extract()

        val targets =
            listOf(
                "Contains unallowed character",
                "Product name is required",
                "Must be no more than 15 characters, including spaces",
            )
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["name"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isIn(targets)
    }

    @Test
    fun `update() - should return 400 when price is 0`() {
        val targetId = 1L
        val response =
            RestAssured
                .given().log().all()
                .body(Product(id = targetId, name = "base", price = 0.0, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/$targetId")
                .then().log().all().extract()

        val expected = "Product price must be greater than zero"
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["price"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `update() - should return 400 when image URL is not valid`() {
        val targetId = 1L
        val response =
            RestAssured
                .given().log().all()
                .body(Product(name = "base", price = 2.0, imageUrl = "ssh://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/$targetId")
                .then().log().all().extract()

        val expected = "Must start with 'https://'."
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["imageUrl"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `update() - should return 400 when name of product already exists`() {
        val targetId = 2L
        val name = "Iron Man"
        val response =
            RestAssured
                .given().log().all()
                .body(Product(id = targetId, name = name, price = 2.0, imageUrl = "https://www.product.com/image/1"))
                .contentType(ContentType.JSON)
                .`when`().put("/api/products/$targetId")
                .then().log().all().extract()

        val expected = "Product with name '$name' already exists."
        val resBody = response.jsonPath().getMap<String, String>("errors")
        val actual = resBody["name"]
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun delete() {
        create()
        val response = controller.deleteProduct(1)
        Assertions.assertThat(response.statusCode.value()).isEqualTo(HttpStatus.NO_CONTENT.value())
        readProducts()
    }

    @Test
    fun `delete() - should return 404 when the product to delete doesn't exist`() {
        val id = 100
        val response =
            RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .`when`().delete("/api/products/$id")
                .then().log().all().extract()

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `delete() - unit test, should return 404 when the product to delete doesn't exist`() {
        create()
        assertThrows<NotFoundException> { controller.deleteProduct(1000) }
    }
}
