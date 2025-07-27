package ecommerce.exception.handler

import ecommerce.controller.MemberController
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [MemberController::class])
class MemberExceptionHandler
// TODO: delete if unused later
