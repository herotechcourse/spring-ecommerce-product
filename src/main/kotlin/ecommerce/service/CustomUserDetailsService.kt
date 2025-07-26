package ecommerce.service

import ecommerce.exception.auth.MemberNotFoundException
import ecommerce.repository.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val member =
            memberRepository.findByEmail(email)
                ?: throw { MemberNotFoundException("Member not found with email $email") } as Throwable
        val authorities = listOf(SimpleGrantedAuthority(member.role.name))
        return User(member.email, member.password, authorities)
    }
}
