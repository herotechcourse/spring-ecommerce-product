package ecommerce

class Product(var id: Long, var name: String, var price: Double, var img: String) {
    fun update(updatedProduct: Product) {
        this.name = updatedProduct.name
        this.price = updatedProduct.price
        this.img = updatedProduct.img
    }

    companion object {
        fun create(
            product: Product,
            id: Long,
        ): Product {
            return Product(product.id, product.name, product.price, product.img)
        }
    }
}
