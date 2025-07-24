package ecommerce.service

import ecommerce.domain.Member
import ecommerce.exception.AuthenticationException
import ecommerce.exception.DuplicateEmailException
import ecommerce.repository.MemberRepository
import ecommerce.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun registerMember(
        userName: String,
        email: String,
        password: String,
    ): String {
        if (memberRepository.findByEmail(email) != null) {
            throw DuplicateEmailException("Member with email $email already exists")
        }
        val hashPass = passwordEncoder.encode(password)

        val newMember =
            Member(
                userName = userName,
                email = email,
                passwordHash = hashPass,
                role = "USER",
            )

        memberRepository.create(newMember)

        return jwtTokenProvider.generateToken(
            newMember.userId,
            memberEmail = newMember.email,
            memberRole = newMember.role,
        )
    }

    fun loginMember(
        email: String,
        password: String,
    ): Pair<Member, String> {
        val member =
            memberRepository.findByEmail(email)
                ?: throw AuthenticationException("Invalid email or password.")

        val passwordMatch = passwordEncoder.matches(password, member.passwordHash)

        if (!passwordMatch) {
            throw AuthenticationException("Wrong email or password")
        }

        val accessToken =
            jwtTokenProvider.generateToken(
                member.userId,
                memberEmail = member.email,
                memberRole = member.role,
            )

        return Pair(member, accessToken)
    }

    fun getMemberById(userId: Long): Member? {
        return memberRepository.findByUserId(userId)
    }
}
