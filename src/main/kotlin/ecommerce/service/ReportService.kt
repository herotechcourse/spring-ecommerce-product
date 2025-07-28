package ecommerce.service

import ecommerce.repository.CartEventRepository
import ecommerce.repository.reportDTO.MemberCartActivityDto
import ecommerce.repository.reportDTO.ProductCartCountDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class ReportService(private val cartEventRepository: CartEventRepository) {
    fun findTop5MostAddedProductsInLast30Days(days: Int): List<ProductCartCountDto> {
        require(days > 0) { "days must be greater than 0" }
        val startDate = LocalDateTime.now().minus(days.toLong(), ChronoUnit.DAYS)
        return cartEventRepository.findTop5MostAddedProductsInLast30Days(startDate)
    }

    fun getMembersWhoAddedItemsInLast7Days(days: Int): List<MemberCartActivityDto> {
        require(days > 0) { "days must be greater than 0" }
        val startDate = LocalDateTime.now().minus(days.toLong(), ChronoUnit.DAYS)
        return cartEventRepository.findMembersWhoAddedItemsInLastDays(startDate)
    }
}
