package ecommerce

import ecommerce.product.DummyRequest
import ecommerce.product.data.ConstantsProduct.Validation.SCHEMA_SQL_URL_LIMIT
import ecommerce.product.data.ProductRequest
import ecommerce.product.data.ProductResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import java.math.BigDecimal

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
            price = BigDecimal("6.50"),
            imageUrl =
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        )

    val AMERICANO =
        ProductRequest(
            name = "Iced Americano T",
            price = BigDecimal("4.50"),
            imageUrl =
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        )

    object Dummy {
        private fun superLongUrl(n: Int): String = "o".repeat(n + 1)

        val NO_NAME =
            DummyRequest(
                price = BigDecimal("6.50"),
                imageUrl =
                    "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            )

        val NO_PRICE =
            DummyRequest(
                name = "Flat white L",
                imageUrl =
                    "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            )

        val NO_URL =
            DummyRequest(
                name = "Flat white L",
                price = BigDecimal("6.50"),
            )

        val SUPER_LONG_URL =
            DummyRequest(
                name = "Flat white L",
                price = BigDecimal("6.50"),
                imageUrl = superLongUrl(IMAGE_URL_MAX_LENGTH),
            )
    }

    object AssertTamplate {
        fun assertProductEquals(
            actual: ProductResponse,
            expected: ProductRequest,
            expectedId: Long,
        ) {
            assertThat(actual.id).isEqualTo(expectedId)
            assertThat(actual.name).isEqualTo(expected.name)
            assertThat(actual.price).isEqualTo(expected.price.toPlainString())
            assertThat(actual.imageUrl).isEqualTo(expected.imageUrl)
        }
    }
}
