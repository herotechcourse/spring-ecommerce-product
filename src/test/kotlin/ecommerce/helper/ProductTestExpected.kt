package ecommerce.helper

import ecommerce.domain.NewProduct
import ecommerce.dto.ProductRequest
import ecommerce.dto.mapper.ProductMapper
import ecommerce.entity.Product

class ProductTestExpected(
    val request: ProductRequest,
    val id: Long = 1L,
) {
    val newProduct: NewProduct =
        NewProduct(
            name = request.name,
            price = request.price,
            imageUrl = request.imageUrl,
        )

    val product: Product = ProductMapper.toEntity(newProduct, id)

    val response = ProductMapper.toResponse(product)
}
