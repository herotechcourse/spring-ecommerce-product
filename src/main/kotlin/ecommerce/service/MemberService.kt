package ecommerce.service

import ecommerce.exception.auth.EmailAlreadyExistsException
import ecommerce.model.Member
import ecommerce.model.MemberRole
import ecommerce.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

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
        name: String,
    ): Member {
        if (memberRepository.findByEmail(email) != null) {
            throw EmailAlreadyExistsException("Member with email $email already exists")
        }
        val hashedPassword = passwordEncoder.encode(password)
        val newMember =
            Member(
                id = UUID.randomUUID(),
                email = email,
                password = hashedPassword,
                role = role,
                name = name,
            )
        return memberRepository.create(newMember)
    }
}
