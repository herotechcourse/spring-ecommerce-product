package ecommerce.api.integration

import ecommerce.model.Member
import ecommerce.model.Product
import ecommerce.service.CartService
import ecommerce.service.StatisticsService
import ecommerce.store.JdbcMemberStore
import ecommerce.store.JdbcProductStore
import org.assertj.core.api.Assertions
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
                    memberRepository.createMember(
                        Member(
                            email = "user$it@example.com",
                            password = "password",
                            name = "name$it",
                        ),
                    )
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

            Assertions.assertThat(topProducts).isNotEmpty
            Assertions.assertThat(activeMembers).hasSize(5)

            println("Top Products: $topProducts")
            println("Active Members: $activeMembers")
        }
    }
