package ecommerce.controller

import ecommerce.dto.RecentActiveMemberResponse
import ecommerce.dto.TopProductStat
import ecommerce.service.StatisticsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/stats")
class AdminStatisticsController(
    private val statisticsService: StatisticsService,
) {
    @GetMapping("/top-products")
    fun getTop5(): ResponseEntity<List<TopProductStat>> {
        val result = statisticsService.getTop5ProductsLast30Days()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/recent-members")
    fun getRecentlyActiveMembers(): ResponseEntity<List<RecentActiveMemberResponse>> {
        val result = statisticsService.getRecentlyActiveMembers()
        return ResponseEntity.ok(result)
    }
}
