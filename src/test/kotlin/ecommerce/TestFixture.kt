package ecommerce

import ecommerce.product.data.ConstantsProduct.Validation.IMAGE_URL_MAX_LENGTH
import ecommerce.product.data.DummyRequest
import ecommerce.product.data.ProductRequest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import java.math.BigDecimal

object TestFixture {
    object ValidRequest {
        val FLAT_WHITE =
            ProductRequest(
                name = "Flat white",
                price = BigDecimal("6.50"),
                imageUrl =
                    "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            )

        val AMERICANO =
            ProductRequest(
                name = "Americano",
                price = BigDecimal("4.50"),
                imageUrl =
                    "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            )
    }

    object InvalidRequest {
        val INVALID_NAME_EXCEED =
            ProductRequest(
                name = "Aaaaaaaamericano",
                price = BigDecimal("4.50"),
                imageUrl =
                    "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            )

        val INVALID_NAME_CHARACTERS =
            ProductRequest(
                name = "Americano!!",
                price = BigDecimal("4.50"),
                imageUrl = "https://example.com/image.jpg",
            )

        val INVALID_PRICE_TOO_SMALL =
            ProductRequest(
                name = "Ice Latte",
                price = BigDecimal("0.01"),
                imageUrl = "https://example.com/image.jpg",
            )

        val INVALID_IMAGE_URL_CHARACTERS =
            ProductRequest(
                name = "Cafe Mocha",
                price = BigDecimal("5.00"),
                imageUrl = "ftp://invalid-url.com/image.jpg",
            )

        val INVALID_IMAGE_URL_EXCEED =
            ProductRequest(
                name = "Cafe Mocha",
                price = BigDecimal("5.00"),
                imageUrl = superLongUrl(),
            )
    }

    object InvalidDummy {
        val NO_NAME =
            DummyRequest(
                price = BigDecimal("6.50"),
                imageUrl =
                    "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            )

        val NO_PRICE =
            DummyRequest(
                name = "Flat white",
                imageUrl =
                    "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            )

        val NO_URL =
            DummyRequest(
                name = "Flat white",
                price = BigDecimal("6.50"),
            )

        val SUPER_LONG_URL =
            DummyRequest(
                name = "Flat white",
                price = BigDecimal("6.50"),
                imageUrl = superLongUrl(),
            )
    }

    fun createTestProduct(request: ProductRequest) {
        val response =
            RestAssured
                .given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .`when`().post("/api/products")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    private fun superLongUrl(): String = "https://" + "o".repeat(IMAGE_URL_MAX_LENGTH - "https://".length + 1)
}
