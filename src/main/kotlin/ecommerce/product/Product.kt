package ecommerce.product

import java.math.BigDecimal

class Product(
    var id: Long? = null,

//    @field:NotBlank(message = "Name cannot be blank")
//    @field:Size(max = 15, message = "Name must be at most 255 characters")
    var name: String?,

//    @field:NotNull(message = "Price is required")
//    @field:Positive(message = "Price must be greater than zero")
    var price: BigDecimal?,
    var imageUrl: String?
) {
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
