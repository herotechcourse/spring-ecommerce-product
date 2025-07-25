package ecommerce

class Product(
    var id: Long? = null,
    var name: String,
    var price: Double,
    var imageUrl: String,
) {
    init {
        require(name.length <= 255)  {RuntimeException("[Error] Name can be maximum 255 characters long.")}
        require(imageUrl.length <= 512) {RuntimeException("[Error] URL can be maximum 512 characters long.")}
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
}
