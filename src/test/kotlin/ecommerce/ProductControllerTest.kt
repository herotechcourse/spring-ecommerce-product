package ecommerce

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.net.URI

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductControllerTest {
    @Test
    fun create() {
        val response =
            RestAssured.given().log().all().body(Product(name = "cola", price = 4.5, imageUrl = "https://cola.jpg"))
                .contentType(ContentType.JSON).`when`().post("/products").then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun read() {
        create()

        val response =
            RestAssured.given().log().all().contentType(ContentType.JSON).`when`().get("/products").then().log().all()
                .extract()

        val rawMap = response.jsonPath().getMap<String, Any>("")
        val mapper = jacksonObjectMapper()
        val typeRef = object : TypeReference<Map<Long, Product>>() {}
        val productMap = mapper.convertValue(rawMap, typeRef)

        assertThat(productMap).hasSize(1)
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
//        assertThat(response.jsonPath().getMap<Long, Product>("", Long::class.java, Product::class.java)).hasSize(1)
    }

    @Test
    fun `update existing product`() {
        create()

        val response =
            RestAssured.given().log().all().body(Product(name = "fanta", price = 5.6, imageUrl = "https://fanta.jpg"))
                .contentType(ContentType.JSON).`when`().put("/products/1").then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun `update non-existing product, test if new product was created`() {

        val responseAfterPut =
            RestAssured.given().log().all().body(Product(name = "fanta", price = 5.6, imageUrl = "https://fanta.jpg"))
                .contentType(ContentType.JSON).`when`().put("/products/3").then().log().all().extract()

        assertThat(responseAfterPut.statusCode()).isEqualTo(HttpStatus.OK.value())

        val responseAfterGet =
            RestAssured.given().log().all().contentType(ContentType.JSON).`when`().get("/products").then().log().all()
                .extract()

        val rawMap = responseAfterGet.jsonPath().getMap<String, Any>("")
        val mapper = jacksonObjectMapper()
        val typeRef = object : TypeReference<Map<Long, Product>>() {}
        val productMap = mapper.convertValue(rawMap, typeRef)

        assertThat(productMap).hasSize(1)
        assertThat(responseAfterGet.statusCode()).isEqualTo(HttpStatus.OK.value())
    }


    @Test
    fun delete() {
        create()

        val response = RestAssured.given().log().all().`when`().delete("/products/1").then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
    }
}


