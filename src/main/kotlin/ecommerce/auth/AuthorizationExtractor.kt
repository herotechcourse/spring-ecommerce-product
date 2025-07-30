package ecommerce.auth

import jakarta.servlet.http.HttpServletRequest

interface AuthorizationExtractor<T> {
    fun extract(request: HttpServletRequest): T

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}
