package ecommerce

import ecommerce.product.Product
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import java.math.BigDecimal

object ProductMock {
    val FLAT_WHITE =
        Product(
            name = "Flat white L",
            price = BigDecimal.valueOf(6.50),
            imageUrl =
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        )

    val AMERICANO =
        Product(
            name = "Iced Americano T",
            price = BigDecimal.valueOf(4.50),
            imageUrl =
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        )

    fun createProduct(product: Product) {
        val response =
            RestAssured
                .given().log().all()
                .body(product)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }
}