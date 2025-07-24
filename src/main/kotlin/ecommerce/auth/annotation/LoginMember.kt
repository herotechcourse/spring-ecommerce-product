package ecommerce.auth.annotation

import kotlin.annotation.AnnotationRetention
import kotlin.annotation.AnnotationTarget

@Target(AnnotationTarget.VALUE_PARAMETER) //  аннотация @LoginMember может использоваться только для параметров функции
@Retention(
    AnnotationRetention.RUNTIME,
) // аннотация @LoginMember будет доступна в байт-коде и её можно будет прочитать во время выполнения программы с помощью рефлексии
annotation class LoginMember
