package ecommerce.model

data class Product(
    var id: Long? = null,
    var name: String,
    var price: Double,
    var imageUrl: String
) {
    fun updateFrom(dto: ProductPatchDTO) {
        name = dto.name ?: name
        price = dto.price ?: price
        imageUrl = dto.imageUrl ?: imageUrl
    }
}
