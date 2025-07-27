package ecommerce.sql

object MemberConstsSQL {
    const val SELECT_BY_ID = "SELECT id, email, password FROM members WHERE id = ?"
    const val SELECT_BY_EMAIL = "SELECT id, email, password FROM members WHERE email = ?"
    const val INSERT =
        "INSERT INTO members (email, password) VALUES (?, ?)"
    const val COUNT_BY_EMAIL = "SELECT count(*) FROM members WHERE email = ?"
}
