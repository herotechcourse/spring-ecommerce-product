package ecommerce.dao

import ecommerce.model.Member

interface MemberDao {
    fun insert(member: Member): Long

    fun findByEmail(email: String): Member?

    fun findById(id: Long): Member?

    fun existsByEmail(email: String): Boolean
}
