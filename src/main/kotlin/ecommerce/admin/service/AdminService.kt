package ecommerce.admin.service

import ecommerce.admin.dto.RecentMemberResponse
import ecommerce.admin.dto.TopProductResponse
import ecommerce.cart.repository.CartRepository
import org.springframework.stereotype.Service

@Service
class AdminService(private val cartRepository: CartRepository) {
    fun getTopProducts(): List<TopProductResponse> {
        return cartRepository.findTopProductsInLast30Days()
    }

    fun getRecentMembers(): List<RecentMemberResponse> {
        return cartRepository.findRecentMembersInLast7Days()
    }
}
