package ecommerce.service

import ecommerce.entity.CartItem
import ecommerce.repository.CartRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    private val repository: CartRepository,
//    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun findByMemberId(memberId: Long): List<CartItem> {
        return repository.selectByMemberId(memberId)
    }
}
