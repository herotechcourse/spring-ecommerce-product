package ecommerce.service

import ecommerce.entity.Member
import ecommerce.exception.InvalidTokenException
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val repository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun findMemberByToken(token: String): Member {
        validateToken(token)
        val id = getPayloadFromToken(token)
        return repository.findById(id)
            ?: throw InvalidTokenException("Member with ID $id not found")
    }

    private fun validateToken(token: String) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw InvalidTokenException()
        }
    }

    private fun getPayloadFromToken(token: String): Long {
        return jwtTokenProvider.getPayload(token).toLongOrNull()
            ?: throw InvalidTokenException("can not parse token")
    }
}
