package ecommerce.annotation

import kotlin.annotation.AnnotationTarget

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginMember
