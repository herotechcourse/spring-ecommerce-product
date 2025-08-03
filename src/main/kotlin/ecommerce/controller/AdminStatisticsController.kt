package ecommerce.controller

import ecommerce.dto.RecentActiveUserResponse
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

    @GetMapping("/recent-users")
    fun getRecentlyActiveUsers(): ResponseEntity<List<RecentActiveUserResponse>> {
        val result = statisticsService.getRecentlyActiveUser()
        return ResponseEntity.ok(result)
    }
}
