package ecommerce.service

import ecommerce.exception.auth.EmailAlreadyExistsException
import ecommerce.model.Member
import ecommerce.model.MemberRole
import ecommerce.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun findByEmail(email: String): Member? {
        return memberRepository.findByEmail(email)
    }

    fun createMember(
        email: String,
        password: String,
        role: MemberRole = MemberRole.ROLE_USER,
    ): Member {
        if (memberRepository.findByEmail(email) != null) {
            throw EmailAlreadyExistsException("Member with email $email already exists")
        }
        val hashedPassword = passwordEncoder.encode(password)
        val newMember =
            Member(
                email = email,
                password = hashedPassword,
                role = role,
            )
        return memberRepository.create(newMember)
    }
}
