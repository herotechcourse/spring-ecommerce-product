package ecommerce.utils

import ecommerce.dto.ProductResponse
import ecommerce.model.Product

fun Product.toResponse(): ProductResponse =
    ProductResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        img = this.img,
        quantity = this.quantity,
    )
