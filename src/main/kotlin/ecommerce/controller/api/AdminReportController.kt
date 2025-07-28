package ecommerce.controller.api

import ecommerce.annotation.LoginMember
import ecommerce.domain.Member
import ecommerce.dto.report.MemberCartActivityDto
import ecommerce.dto.report.ProductCartCountDto
import ecommerce.service.ReportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/reports")
class AdminReportController(private val reportService: ReportService) {
    @GetMapping("/top-products-30-days")
    fun findTop5MostAddedProductsInLast30Days(
        @LoginMember admin: Member,
    ): ResponseEntity<List<ProductCartCountDto>> {
        val topProducts = reportService.findTop5MostAddedProductsInLast30Days()
        return ResponseEntity.ok(topProducts)
    }

    @GetMapping("/members-added-to-cart-7-days")
    fun getMembersWhoAddedItemsInLast7Days(
        @LoginMember admin: Member,
    ): ResponseEntity<List<MemberCartActivityDto>> {
        println("Admin ${admin.userName} (ID: ${admin.userId}) is requesting members who added items report.")
        val members = reportService.getMembersWhoAddedItemsInLast7Days()
        return ResponseEntity.ok(members)
    }
}
