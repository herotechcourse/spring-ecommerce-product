package ecommerce.sql

object CartConstsSQL {
    const val INSERT = "INSERT INTO cart_items (member_id, product_id, quantity) VALUES (?, ?, ?)"
    const val SELECT_BY_MEMBER =
        " SELECT id, member_id, product_id, quantity, created_at, updated_at " +
            "FROM cart_items WHERE member_id = ?"
    const val COUNT_BY_MEMBER = "SELECT count(*) FROM cart_items WHERE member_id = ?"
}
