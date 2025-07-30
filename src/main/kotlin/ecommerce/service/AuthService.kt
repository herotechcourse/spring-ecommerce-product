package ecommerce.service

import ecommerce.entity.Member
import ecommerce.exception.InvalidTokenException
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val repository: MemberRepository,
    private val jwtProvider: JwtProvider,
) {
    fun findMemberByToken(token: String): Member {
        if (!jwtProvider.validateToken(token)) {
            throw InvalidTokenException()
        }
        val id =
            jwtProvider.getPayload(token).toLongOrNull()
                ?: throw InvalidTokenException("can not parse token")

        return repository.findById(id)
            ?: throw InvalidTokenException("Member with ID $id not found")
    }
}
