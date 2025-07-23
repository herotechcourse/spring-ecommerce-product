package ecommerce.model

data class Product(
    var id: Long? = null,
    var name: String,
    var price: Double,
    var imageUrl: String,
) {
    companion object {
        fun toEntity(
            product: Product,
            id: Long,
        ): Product {
            return Product(id, product.name, product.price, product.imageUrl)
        }
    }
}
