package ecommerce.entity

@JvmInline
value class Price(val value: Double) {
    init {
        require(value >= 0.01) { "Price must be greater than 0." }
    }
}
