package ecommerce.dto

import ecommerce.model.Role

data class MemberResponse(
    var id: Long,
    var email: String,
    var role: Role = Role.USER,
    var name: String,
)
