package ecommerce.repository

import ecommerce.domain.CartEvent
import org.springframework.data.jpa.repository.JpaRepository

interface CartEventRepository : JpaRepository<CartEvent, Long>
