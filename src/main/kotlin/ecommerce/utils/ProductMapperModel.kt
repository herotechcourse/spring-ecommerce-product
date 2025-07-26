package ecommerce.utils

import ecommerce.dto.CreateProductRequest
import ecommerce.dto.UpdateProductRequest
import ecommerce.model.Product

fun CreateProductRequest.toModel(): Product =
    Product(
        id = 0L,
        name = this.name,
        price = this.price,
        img = this.img,
        quantity = this.quantity,
    )

fun UpdateProductRequest.toModel(id: Long): Product =
    Product(
        id = id,
        name = this.name,
        price = this.price,
        img = this.img,
        quantity = this.quantity,
    )
