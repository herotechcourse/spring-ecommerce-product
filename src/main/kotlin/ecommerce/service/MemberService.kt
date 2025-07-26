package ecommerce.service

import ecommerce.dto.MemberRequest
import ecommerce.dto.TokenResponse
import ecommerce.dto.mapper.MemberMapper
import ecommerce.exception.CanNotInsertWithKeyHolderException
import ecommerce.exception.MemberAlreadyExistsException
import ecommerce.exception.RetrievalFailedException
import ecommerce.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val repository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun registerByEmail(request: MemberRequest): TokenResponse {
        if (repository.existsByEmail(request.email)) {
            throw MemberAlreadyExistsException("Email ${request.email} already exists")
        }

        val id =
            repository.insert(request)
                ?: throw CanNotInsertWithKeyHolderException("Failed to insert member with email ${request.email}")

        val member =
            repository.findById(id)
                ?: throw RetrievalFailedException("Member with ID $id could not be retrieved after insertion")

        val token = jwtTokenProvider.createToken(member)

        return MemberMapper.toResponse(token)
    }
}
