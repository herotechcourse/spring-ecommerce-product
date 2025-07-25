package ecommerce.dto

import java.sql.Timestamp

data class TopProductStats(
    val productName: String,
    val addedCount: Int,
    val lastAddedAt: Timestamp,
)
