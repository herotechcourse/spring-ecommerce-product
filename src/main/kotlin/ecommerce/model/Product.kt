package ecommerce.model

import ecommerce.dto.ProductPatchDTO

data class Product(
    var name: String,
    var price: Double,
    var imageUrl: String,
    var id: Long? = null,
) {
    fun updateFrom(dto: ProductPatchDTO) {
        name = dto.name ?: name
        price = dto.price ?: price
        imageUrl = dto.imageUrl ?: imageUrl
    }
}
