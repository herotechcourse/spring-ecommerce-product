package ecommerce

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class Product(
    var id: Long? = null,

    @Size(max = 15, message = "Product name cannot exceed 15 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9()\\[\\]+\\-&/_]+$",
        message = "Only letters, digits and these special characters are allowed: () [] + - & / _"
    )
    var name: String = "",

    @Positive(message = "Price must be > 0")
    var price: Double = 0.0,

    @Pattern(regexp = "^https?://.*", message = "URL must start with https:// or http://" )
    var imageUrl: String = "",
) {
    override fun equals(other: Any?): Boolean {
        if (this == other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Product

        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: System.identityHashCode(this)
    }
}
