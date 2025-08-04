package ecommerce.controller.api

import ecommerce.dto.MemberDTO
import ecommerce.dto.ProductStatsDTO
import ecommerce.service.StatisticsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stats")
class StatisticsController(private val statisticsService: StatisticsService) {
    @GetMapping("/top-products")
    fun getTopProducts(): ResponseEntity<List<ProductStatsDTO>> {
        val stats = statisticsService.getTop5MostAddedProductsLast30Days()
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/recent-active-members")
    fun getRecentActiveMembers(): ResponseEntity<List<MemberDTO>> {
        val members = statisticsService.getMembersAddedToCartLast7Days()
        return ResponseEntity.ok(members)
    }
}
