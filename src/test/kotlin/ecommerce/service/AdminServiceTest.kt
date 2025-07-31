package ecommerce.service

import ecommerce.repository.CartItemRepository
import ecommerce.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.jdbc.Sql

@DataJpaTest
@ComponentScan(basePackages = ["ecommerce.repository"])
class AdminServiceTest
    @Autowired
    constructor(
        private val cartItemRepository: CartItemRepository,
        private val memberRepository: MemberRepository,
        private val entityManager: TestEntityManager,
    ) {
        private lateinit var adminService: AdminService

        @BeforeEach
        fun setUp() {
            adminService = AdminService(cartItemRepository, memberRepository)
        }

        @Test
        fun `getTop5MostAddedProducts should return empty list if no products added`() {
            val topProducts = adminService.getTop5MostAddedProducts()

            assertThat(topProducts).isNotNull
            assertThat(topProducts).isEmpty()
        }

        @Test
        @Sql(
            scripts = ["/sql/clean-cartitems.sql"],
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        )
        fun `getRecentlyActiveMembers should return empty list if no members active`() {
            val activeMembers = adminService.getRecentlyActiveMembers()

            assertThat(activeMembers).isNotNull
            assertThat(activeMembers).isEmpty()
        }
    }
