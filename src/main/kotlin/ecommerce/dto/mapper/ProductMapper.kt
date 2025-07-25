package ecommerce.dto.mapper

import ecommerce.dto.ProductRequest
import ecommerce.dto.ProductResponse
import ecommerce.entity.Product

object ProductMapper {
    fun toResponse(product: Product): ProductResponse {
        return ProductResponse(
            product.id,
            product.name,
            product.price.toPlainString(),
            product.imageUrl,
        )
    }

    fun toEntity(
        request: ProductRequest,
        id: Long,
    ): Product {
        return Product(
            id = id,
            name = request.name,
            price = request.price,
            imageUrl = request.imageUrl,
        )
    }
}
