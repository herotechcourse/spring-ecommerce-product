package ecommerce.dto

import java.net.URI

data class UserCreateResponse(
    val uri: URI,
    val token: String,
)
