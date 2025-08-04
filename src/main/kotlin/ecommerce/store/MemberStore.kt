package ecommerce.store

import ecommerce.model.Member

interface MemberStore {
    fun createMember(member: Member): Member

    fun findAllMembers(): List<Member>

    fun findMemberByEmail(email: String): Member

    fun findMemberById(id: Long): Member?
}
