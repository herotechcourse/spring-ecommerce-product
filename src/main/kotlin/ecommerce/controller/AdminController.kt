package ecommerce.controller

import ecommerce.dto.MemberStatsResponse
import ecommerce.dto.ProductStatsResponse
import ecommerce.dto.StatsQueryParams
import ecommerce.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(private val service: AdminService) {
    @GetMapping("/stats/products/top")
    fun getTopProducts(): ResponseEntity<List<ProductStatsResponse>> {
        val products = service.getTopProducts(TOP_5_LAST_30_DAYS)
        return ResponseEntity.ok(products)
    }

    @GetMapping("/stats/members/active")
    fun getActiveMembers(): ResponseEntity<List<MemberStatsResponse>> {
        val products = service.getActiveMembers(ACTIVE_MEMBERS_LAST_7_DAYS)
        return ResponseEntity.ok(products)
    }

    companion object {
        val TOP_5_LAST_30_DAYS =
            StatsQueryParams(
                limit = 5,
                days = 30,
                sort = "add_count DESC",
            )

        val ACTIVE_MEMBERS_LAST_7_DAYS =
            StatsQueryParams(
                limit = Int.MAX_VALUE,
                days = 7,
                sort = "m.id DESC",
            )
    }
}
