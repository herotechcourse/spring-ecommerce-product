package ecommerce.dto

import java.time.LocalDateTime

class TopProductStat(
    val productId: Long,
    val productName: String,
    val addedCount: Int,
    val lastAddedAt: LocalDateTime,
)
