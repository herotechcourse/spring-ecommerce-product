package ecommerce.product

class Product(var id: Long? = null, var name: String?, var price: Double?, var imageUrl: String?) {
    //    fun update(newProduct: Product) {
//        this.name = newProduct.name
//        this.price = newProduct.price
//        this.imageUrl = newProduct.imageUrl
//    }
    fun updateWith(partial: Product): Product {
        return Product(
            name = partial.name ?: this.name,
            price = partial.price ?: this.price,
            imageUrl = partial.imageUrl ?: this.imageUrl
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
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
