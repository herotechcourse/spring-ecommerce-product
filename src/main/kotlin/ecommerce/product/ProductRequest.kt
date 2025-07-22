package ecommerce.product

data class ProductRequest(
    val name: String,
    val price: String,
    val imageUrl: String,
)

fun ProductRequest.toEntity(id: Long): Product {
    return Product(
        id = id,
        name = this.name,
        price = this.price.toBigDecimal(),
        imageUrl = this.imageUrl,
    )
}
