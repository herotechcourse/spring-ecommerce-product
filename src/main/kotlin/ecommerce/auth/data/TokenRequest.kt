package ecommerce.auth.data

data class TokenRequest(
//    @field:Size()
//    @field:Pattern()
    val email: String,
//    @field:Size()
//    @field:Pattern()
    val password: String,
)
