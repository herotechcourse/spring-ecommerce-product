package ecommerce.entity

class User(
    var id: Long? = null,
    var email: String,
    var passwordHash: String,
    var role: String = "USER",
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
