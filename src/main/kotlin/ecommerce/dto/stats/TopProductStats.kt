package ecommerce.dto.stats

import java.time.LocalDateTime

data class TopProductStats(
    val productId: Long,
    val productName: String,
    val timesAdded: Int,
    val mostRecentAddedTime: LocalDateTime,
)
