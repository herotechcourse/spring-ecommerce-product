package ecommerce.api.statistics

import ecommerce.auth.model.Member
import ecommerce.auth.store.JdbcMemberStore
import ecommerce.cart.service.CartService
import ecommerce.products.model.Product
import ecommerce.products.store.JdbcProductStore
import ecommerce.statistics.service.StatisticsService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class StatisticsIntegrationTest
    @Autowired
    constructor(
        val memberRepository: JdbcMemberStore,
        val productRepository: JdbcProductStore,
        val cartService: CartService,
        val statisticsService: StatisticsService,
    ) {
        @Test
        fun `create 5 members and products and calculate statistics`() {
            val members =
                (1..5).map {
                    memberRepository.createMember(Member(email = "user$it@example.com", password = "password", name = "name$it"))
                }

            val products =
                (1..5).map {
                    productRepository.insertProduct(
                        Product(
                            name = "Product$it",
                            price = 100.0 * it,
                            imageUrl = "http://image$it.url",
                        ),
                    )
                }

            members.forEachIndexed { index, member ->
                val cart = cartService.getOrCreateCart(member.id!!)
                val productToAdd = products[index % products.size]
                cartService.addItem(member.id!!, productToAdd.id!!, quantity = index + 1) // quantity as 1,2,3,...
            }

            val topProducts = statisticsService.getTop5MostAddedProductsLast30Days()
            val activeMembers = statisticsService.getMembersAddedToCartLast7Days()

            assertThat(topProducts).isNotEmpty
            assertThat(activeMembers).hasSize(5)

            println("Top Products: $topProducts")
            println("Active Members: $activeMembers")
        }
    }
