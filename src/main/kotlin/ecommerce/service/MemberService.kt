package ecommerce.service

import ecommerce.exception.DuplicateMemberEmailException
import ecommerce.model.Member
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val jdbc: MemberRepository,
) {
    fun validateUniqueName(member: Member) {
        when {
            jdbc.existsByEmail(member.email) -> throw DuplicateMemberEmailException(
                "Member with email '${member.email}' already exists.",
            )
        }
    }

    fun findByEmail(email: String): Member? {
        return jdbc.findByEmail(email)
    }

    fun existsByEmail(email: String): Boolean {
        return jdbc.existsByEmail(email)
    }

    fun create(member: Member): Long? {
        return jdbc.create(member)
    }
}
