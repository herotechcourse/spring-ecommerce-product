package ecommerce.product

class Product(
    var id: Long? = null,
    var name: String,
    var price: Double,
    var imageUrl: String?
) {
    fun update(newProduct: Product) {
        this.name = newProduct.name
        this.price = newProduct.price
        this.imageUrl = newProduct.imageUrl
    }

    companion object {
        fun withId(
            product: Product,
            id: Long,
        ): Product {
            return Product(id, product.name, product.price, product.imageUrl)
        }
    }
}
