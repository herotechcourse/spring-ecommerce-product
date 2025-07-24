package ecommerce.model

data class Product(
    val id: Long? = null,
    val name: String,
    val price: Double,
    val imageUrl: String,
) {
    init {
        require(price > 0) { "Product price must be positive, got: $price" }
        require(name.isNotBlank()) { "Product name cannot be blank" }
        require(imageUrl.isNotBlank()) { "Product imageUrl cannot be blank" }
    }

    fun copyFrom(other: Product): Product =
        this.copy(
            name = other.name,
            price = other.price,
            imageUrl = other.imageUrl,
        )

    fun partialUpdate(other: Product): Product =
        this.copy(
            name = other.name,
            price = other.price,
            imageUrl = other.imageUrl,
        )

    companion object {
        fun toEntity(
            id: Long,
            product: Product,
        ): Product {
            return Product(id, product.name, product.price, product.imageUrl)
        }
    }
}
