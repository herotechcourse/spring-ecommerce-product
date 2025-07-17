package ecommerce.product

class Product(var id: Long? = null, var name: String, var price: Double, var imageURL: String?) {
    fun update(newProduct: Product) {
        this.name = newProduct.name
        this.price = newProduct.price
        this.imageURL = newProduct.imageURL
    }

    companion object {
        fun toEntity(
            product: Product,
            id: Long,
        ): Product {
            return Product(id, product.name, product.price, product.imageURL)
        }
    }
}
