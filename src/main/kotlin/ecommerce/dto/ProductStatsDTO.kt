package ecommerce.dto

import java.time.LocalDateTime

data class ProductStatsDTO(
    val name: String,
    val timesAdded: Int,
    val mostRecentAddedTime: LocalDateTime,
)
