package ecommerce.exception

class UserCredentialException(email: String) : RuntimeException("Entered Password is incorrect: $email")
