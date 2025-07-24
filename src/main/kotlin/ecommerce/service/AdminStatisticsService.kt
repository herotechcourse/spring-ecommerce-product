package ecommerce.service

import ecommerce.dto.cartStatistics.MembersWhoAddedToCartDTO
import ecommerce.dto.cartStatistics.TopAddedProductsDTO
import ecommerce.repository.CartStatisticsRepository
import org.springframework.stereotype.Service

@Service
class AdminStatisticsService(
    private val cartStatisticsRepository: CartStatisticsRepository,
) {
    fun getTopAddedProducts(limit: Int = 5): List<TopAddedProductsDTO> {
        return cartStatisticsRepository.getTopAddedProducts(limit)
    }

    fun getMembersWhoAddedToCart(days: Int = 7): List<MembersWhoAddedToCartDTO> {
        return cartStatisticsRepository.getMembersWhoAddedToCart(days)
    }
}