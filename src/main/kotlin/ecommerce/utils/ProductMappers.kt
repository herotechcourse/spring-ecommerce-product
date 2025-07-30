package ecommerce.utils

import ecommerce.domain.Product
import ecommerce.dto.product.CreateProductRequest
import ecommerce.dto.product.ProductResponse
import ecommerce.dto.product.UpdateProductRequest

fun CreateProductRequest.toDomain(): Product =
    Product(
        id = 0L,
        name = this.name,
        price = this.price,
        img = this.img,
        quantity = this.quantity,
    )

fun UpdateProductRequest.toDomain(id: Long): Product =
    Product(
        id = id,
        name = this.name,
        price = this.price,
        img = this.img,
        quantity = this.quantity,
    )

fun Product.toResponse(): ProductResponse =
    ProductResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        img = this.img,
        quantity = this.quantity,
    )
