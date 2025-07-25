package ecommerce.auth.helper

import ecommerce.auth.data.MemberRequest
import io.jsonwebtoken.Jwts

object MemberTestFixture {
    object RequestCases {
        val VALID_ADMIN =
            MemberRequest(
                email = "admin@email.com",
                password = "password",
            )
    }

    object ValidationCase {
        val DEFAULT_CASE =
            ValidationTestSet(
                key = "TGZtZ3VyaU94UVZ1d2k2NGhNVmxuY3AzZ1JHVWltcE5HTnZkNWQzZg==",
                validityInMilliseconds = 3600000,
                algorithm = Jwts.SIG.HS256,
            )
    }
}
