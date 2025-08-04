package ecommerce.service

import ecommerce.exception.EmailAlreadyInUseException
import ecommerce.model.Member
import ecommerce.store.MemberStore
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberStore: MemberStore) {
    fun create(member: Member): Member {
        val allMembers = findAll()
        val emailAlreadyExists = allMembers.any { it.email == member.email }
        if (emailAlreadyExists) {
            throw EmailAlreadyInUseException("Email already exists")
        }
        return memberStore.createMember(member)
    }

    fun findAll(): List<Member> = memberStore.findAllMembers()

    fun findMember(email: String) = memberStore.findMemberByEmail(email)

    fun findMemberById(id: Long) = memberStore.findMemberById(id)
}
