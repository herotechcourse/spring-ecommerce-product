package ecommerce.config

import ecommerce.annotation.AdminOnly
import ecommerce.exception.UnauthorizedException
import ecommerce.model.Member
import ecommerce.model.Role
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RoleCheckInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler is HandlerMethod) {
            val method = handler.method

            val hasAnnotation = method.getAnnotation(AdminOnly::class.java) != null

            if (hasAnnotation) {
                val member = request.getAttribute("member") as? Member

                if (member == null || member.role != Role.ADMIN) {
                    throw UnauthorizedException()
                }
            }
        }

        return true
    }
}
