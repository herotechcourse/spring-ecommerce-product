package ecommerce.model

class Cart(
    val id: Long? = null,
    val userId: Long,
    val products: MutableList<Product> = mutableListOf(),
)
