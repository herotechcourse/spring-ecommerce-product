package ecommerce.repository.reportDTO

import java.time.LocalDateTime

data class ProductCartCountDto(
    val productId: Long,
    val productName: String,
    val addedCount: Long,
    val lastAddedTime: LocalDateTime,
)
