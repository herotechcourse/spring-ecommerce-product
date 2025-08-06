package ecommerce.sql

enum class SortOption(val sql: String) {
    PRODUCT_COUNT("add_count DESC"),
    MEMBER_EMAIL("m.email ASC"),
    ;

    companion object {
        fun from(value: SortOption): SortOption? =
            SortOption.entries.find {
                it == value
            }
    }
}
