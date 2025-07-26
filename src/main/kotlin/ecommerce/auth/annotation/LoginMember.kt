package ecommerce.auth.annotation

import kotlin.annotation.AnnotationRetention
import kotlin.annotation.AnnotationTarget

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(
    AnnotationRetention.RUNTIME,
)
annotation class LoginMember
