package ecommerce.sql

object MemberConstsSQL {
    const val COUNT_ALL = "SELECT count(*) FROM memebers"
    const val SELECT_ALL = "SELECT id, email, password FROM members"
    const val SELECT_BY_ID = "SELECT id, email, password FROM members WHERE id = ?"
    const val INSERT =
        "INSERT INTO members (email, password) VALUES (?, ?)"
    const val UPDATE_BY_ID = " UPDATE members SET email = ?, password = ? WHERE id = ?"
    const val DELETE_BY_ID = "DELETE FROM members WHERE id = ?"
    const val COUNT_BY_ID = "SELECT count(*) FROM members WHERE id = ?"
    const val COUNT_BY_EMAIL = "SELECT count(*) FROM members WHERE email = ?"
}
