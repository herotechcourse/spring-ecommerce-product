package ecommerce.admin.dto

import java.time.LocalDateTime

class TopProductResponse(
    val productName: String,
    val addCount: Long,
    val lastAddedAt: LocalDateTime,
)
