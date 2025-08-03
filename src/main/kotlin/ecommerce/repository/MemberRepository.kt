package ecommerce.repository

import ecommerce.model.Member

interface MemberRepository {
    fun create(member: Member): Long?

    fun findByEmail(email: String): Member?

    fun existsByEmail(email: String): Boolean
}
