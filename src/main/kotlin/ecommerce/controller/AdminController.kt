package ecommerce.controller

import ecommerce.dto.MemberStatsResponse
import ecommerce.dto.ProductStatsResponse
import ecommerce.dto.StatsQueryParams
import ecommerce.service.AdminService
import ecommerce.sql.SortOption
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(private val service: AdminService) {
    @GetMapping("/stats/products/top")
    fun getTopProducts(): List<ProductStatsResponse> = service.getTopProducts(TOP_5_LAST_30_DAYS)

    @GetMapping("/stats/members/active")
    fun getActiveMembers(): List<MemberStatsResponse> = service.getActiveMembers(ACTIVE_MEMBERS_LAST_7_DAYS)

    companion object {
        val TOP_5_LAST_30_DAYS =
            StatsQueryParams(
                limit = 5,
                days = 30,
                sort = SortOption.PRODUCT_COUNT,
            )

        val ACTIVE_MEMBERS_LAST_7_DAYS =
            StatsQueryParams(
                limit = Int.MAX_VALUE,
                days = 7,
                sort = SortOption.MEMBER_EMAIL,
            )
    }
}
