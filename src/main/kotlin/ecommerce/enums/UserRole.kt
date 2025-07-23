package ecommerce.enums

enum class UserRole(val value: String) {
    USER("user"),
    ADMIN("admin"),
    ;

    companion object {
        fun fromValue(value: String): UserRole =
            entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown role: $value")
    }
}
