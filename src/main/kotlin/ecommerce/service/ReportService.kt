package ecommerce.service

import ecommerce.dto.report.MemberCartActivityDto
import ecommerce.dto.report.ProductCartCountDto
import ecommerce.repository.CartEventRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class ReportService(private val cartEventRepository: CartEventRepository) {
    fun findTop5MostAddedProductsInLast30Days(): List<ProductCartCountDto> {
        val startDate = LocalDateTime.now().minus(30, ChronoUnit.DAYS)
        return cartEventRepository.findTop5MostAddedProductsInLast30Days(startDate)
    }

    fun getMembersWhoAddedItemsInLast7Days(): List<MemberCartActivityDto> {
        val startDate = LocalDateTime.now().minus(7, ChronoUnit.DAYS)
        return cartEventRepository.findMembersWhoAddedItemsInLastDays(startDate)
    }
}
