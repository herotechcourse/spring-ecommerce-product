package ecommerce.entity

class User(
    var id: Long? = null,
    var email: String,
    var password: String,
    var role: String? = null,
) {
    fun checkPassword(providedPassword: String): Boolean {
        return providedPassword == password
    }

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
