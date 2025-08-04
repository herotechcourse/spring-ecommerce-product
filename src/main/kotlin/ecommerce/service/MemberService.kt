package ecommerce.service

import ecommerce.dto.MemberRequestDTO
import ecommerce.exception.EmailAlreadyInUseException
import ecommerce.model.Member
import ecommerce.store.MemberStore
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberStore: MemberStore) {
    fun create(memberRequest: MemberRequestDTO): Member {
        val allMembers = findAll()
        val emailAlreadyExists = allMembers.any { it.email == memberRequest.email }
        if (emailAlreadyExists) {
            throw EmailAlreadyInUseException("Email already exists")
        }
        return memberStore.createMember(memberRequest.toEntity())
    }

    fun findAll(): List<Member> = memberStore.findAllMembers()

    fun findMember(email: String) = memberStore.findMemberByEmail(email)

    fun findMemberById(id: Long) = memberStore.findMemberById(id)
}
