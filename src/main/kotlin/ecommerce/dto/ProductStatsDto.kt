package ecommerce.dto

import java.time.LocalDateTime

class ProductStatsDto(
    val productName: String,
    val productQuantity: Long,
    val mostRecent: LocalDateTime,
)
