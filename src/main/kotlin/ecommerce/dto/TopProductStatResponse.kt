package ecommerce.dto

import java.sql.Timestamp

data class TopProductStatResponse(
    val name: String,
    val count: Int,
    val lastAddedAt: Timestamp,
)
