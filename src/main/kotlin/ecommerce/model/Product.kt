package ecommerce.model

class Product(
    val id: Long? = null,
    var name: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
) {
    fun update(newProduct: Product) {
        this.name = newProduct.name
        this.price = newProduct.price
        this.imageUrl = newProduct.imageUrl
    }

    companion object {
        fun toEntity(
            product: Product,
            id: Long,
        ): Product {
            return Product(id, product.name, product.price, product.imageUrl)
        }
    }
}
