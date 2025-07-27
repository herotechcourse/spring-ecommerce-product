package ecommerce.service

import ecommerce.dto.stats.ActiveMemberResponse
import ecommerce.dto.stats.TopProductStats
import ecommerce.repository.CartItemRepository
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminService(
    private val cartItemRepository: CartItemRepository,
    private val memberRepository: MemberRepository,
) {
    fun getTop5MostAddedProducts(): List<TopProductStats> {
        val thirtyDayAgo = LocalDateTime.now().minusDays(30)
        return cartItemRepository.findTop5AddedProducts(thirtyDayAgo)
    }

    fun getRecentlyActiveMembers(): List<ActiveMemberResponse> {
        val sevenDaysAgo = LocalDateTime.now().minusDays(7)
        val activeMemberIds = cartItemRepository.findRecentlyActiveMemberIds(sevenDaysAgo)
        val activeMembers = memberRepository.findByIds(activeMemberIds)
        return activeMembers.map { member ->
            ActiveMemberResponse(
                memberId = member.id,
                memberName = member.name,
                memberEmail = member.email,
            )
        }
    }
}
