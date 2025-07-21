package ecommerce.model

data class Product(
    val id: Long? = null,
    var name: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
)
