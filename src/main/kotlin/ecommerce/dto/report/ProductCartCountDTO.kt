package ecommerce.dto.report

import java.time.LocalDateTime

data class ProductCartCountDTO(
    val productId: Long,
    val productName: String,
    val addedCount: Long,
    val lastAddedTime: LocalDateTime,
)
