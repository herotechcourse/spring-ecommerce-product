package ecommerce.controller.api

import ecommerce.dao.JdbcCartDAO
import ecommerce.dto.ActiveMemberInfo
import ecommerce.dto.TopProductStats
import ecommerce.exception.InternalServerErrorException
import ecommerce.model.Member
import ecommerce.ui.LoginMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/cart-stats")
class CartStatsController(
    private val jdbcCartDAO: JdbcCartDAO,
) {
    @GetMapping("/top5-products")
    fun getTop5Products(
        @LoginMember member: Member,
    ): ResponseEntity<List<TopProductStats>> {
        member.id ?: throw InternalServerErrorException("auth failed")
        val stats = jdbcCartDAO.getTop5AddedProductsInLast30Days()
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/active-members")
    fun getActiveMembers(
        @LoginMember member: Member,
    ): ResponseEntity<List<ActiveMemberInfo>> {
        member.id ?: throw InternalServerErrorException("auth failed")
        val members = jdbcCartDAO.getActiveMembersInLast7Days()
        return ResponseEntity.ok(members)
    }
}
