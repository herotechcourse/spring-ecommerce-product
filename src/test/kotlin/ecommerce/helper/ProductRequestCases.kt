package ecommerce.helper

import ecommerce.constants.ConstantsProduct
import ecommerce.dto.ProductRequest
import java.math.BigDecimal

object ProductRequestCases {
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

    object InvalidRequest {
        val INVALID_NAME_EXCEED =
            ProductRequest(
                name = "Aaaaaaaamericano",
                price = BigDecimal("4.50"),
                imageUrl =
                    "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            )

        val INVALID_NAME_BLANKS =
            ProductRequest(
                name = "Americano!!",
                price = BigDecimal("4.50"),
                imageUrl = "https://example.com/image.jpg",
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
                price = BigDecimal("0.00"),
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

        val INVALID_IMAGE_URL_BLANKS =
            ProductRequest(
                name = "Cafe Mocha",
                price = BigDecimal("5.00"),
                imageUrl = "",
            )

        val SUPER_LONG_URL =
            ProductRequest(
                name = "Flat white",
                price = BigDecimal("6.50"),
                imageUrl = superLongUrl(),
            )
    }

    private fun superLongUrl(): String = "https://" + "o".repeat(ConstantsProduct.Validation.IMAGE_URL_MAX_LENGTH - "https://".length + 1)
}
