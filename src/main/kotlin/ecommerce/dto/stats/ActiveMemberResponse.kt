package ecommerce.dto.stats

import java.util.UUID

data class ActiveMemberResponse(
    val memberId: UUID,
    val memberName: String,
    val memberEmail: String,
)
