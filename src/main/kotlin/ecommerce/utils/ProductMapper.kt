package ecommerce.utils

import ecommerce.dto.CreateProductRequest
import ecommerce.dto.ProductResponse
import ecommerce.dto.UpdateProductRequest
import ecommerce.model.Product

fun Product.toResponse() = ProductResponse(id, name, price, img, quantity)

fun CreateProductRequest.toModel() = Product(0, name, price, img, quantity)

fun UpdateProductRequest.toModel(id: Long) = Product(id, name, price, img, quantity)
