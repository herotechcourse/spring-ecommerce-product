package ecommerce.auth.service

import ecommerce.auth.model.Member
import ecommerce.auth.store.MemberStore
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberStore: MemberStore) {
    fun create(member: Member): Member = memberStore.createMember(member)

    fun findAll(): List<Member> = memberStore.findAllMembers()

    fun findMember(email: String) = memberStore.findMemberByEmail(email)
}
