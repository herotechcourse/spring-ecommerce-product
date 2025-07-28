package ecommerce.dto

import java.time.LocalDateTime

class ProductStatsResponse(
    val productId: Long,
    val addCount: Long,
    val lastAdded: LocalDateTime,
)
