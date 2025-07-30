package ecommerce.dto.mapper

import ecommerce.domain.NewProduct
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

    fun toNewProduct(request: ProductRequest): NewProduct {
        return NewProduct(
            name = request.name,
            price = request.price,
            imageUrl = request.imageUrl,
        )
    }

    fun toEntity(
        newProduct: NewProduct,
        id: Long,
    ): Product {
        return Product(
            id = id,
            name = newProduct.name,
            price = newProduct.price,
            imageUrl = newProduct.imageUrl,
        )
    }
}
