package ecommerce.domain

import java.time.LocalDateTime

data class Cart(var id: Long = 0, val memberId: Long, val createdAt: LocalDateTime)
