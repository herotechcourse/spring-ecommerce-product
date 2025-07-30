package ecommerce.repository.reportDto

import java.time.LocalDateTime

data class ProductCartCountDto(
    val productId: Long,
    val productName: String,
    val addedCount: Long,
    val lastAddedTime: LocalDateTime,
)
