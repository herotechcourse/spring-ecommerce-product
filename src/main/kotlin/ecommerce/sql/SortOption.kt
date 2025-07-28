package ecommerce.sql

enum class SortOption(val sql: String) {
    PRODUCT_COUNT("add_count DESC"),
    PRODUCT_RECENT("last_added_at DESC"),
    MEMBER_RECENT("m.id DESC"),
    MEMBER_EMAIL("m.email ASC"),
    ;

    companion object {
        fun from(value: String): SortOption? = SortOption.entries.find { it.name.equals(value, ignoreCase = true) }
    }
}
