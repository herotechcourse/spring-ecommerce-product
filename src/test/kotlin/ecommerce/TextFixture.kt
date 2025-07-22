package ecommerce

import ecommerce.product.ProductRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

object TextFixture {
    fun createProduct(request: ProductRequest) {
        val response =
            RestAssured
                .given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    val FLAT_WHITE =
        ProductRequest(
            name = "Flat white L",
            price = "6.50",
            imageUrl =
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        )

    val AMERICANO =
        ProductRequest(
            name = "Iced Americano T",
            price = "4.50",
            imageUrl =
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        )
}
