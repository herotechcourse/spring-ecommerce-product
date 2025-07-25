package ecommerce.domain

data class Product(var id: Long = 0, var name: String, var price: Double, var img: String, var quantity: Int)
