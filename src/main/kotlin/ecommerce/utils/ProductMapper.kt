package ecommerce.utils

import ecommerce.dto.product.ProductRequest
import ecommerce.dto.product.ProductResponse
import ecommerce.model.Product

fun Product.toResponse() = ProductResponse(id, name, price, img, quantity)

fun ProductRequest.toModel(id: Long) = Product(id, name, price, img, quantity)
