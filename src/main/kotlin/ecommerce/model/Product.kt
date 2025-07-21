package ecommerce.model

data class Product(
    var id: Long? = null,
    var name: String,
    var price: Double,
    var imageUrl: String,
)
