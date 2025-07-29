package ecommerce.controller

import ecommerce.repository.CartRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/admin/analytics")
@RestController
class AnalyticsController(
    private val cartRepository: CartRepository,
) {
    @GetMapping("/top-products")
    fun getTopProducts(): List<Map<String, Any?>> {
        return cartRepository.findTop5MostAddedProductsLast30Days()
    }

    @GetMapping("/active-users")
    fun getActiveUsers(): List<Map<String, Any?>> {
        return cartRepository.findMembersActiveInLast7Days()
    }
}
