package ecommerce.sql

object ConstantsSQL {
    const val COUNT_ALL = "SELECT count(*) FROM products"
    const val SELECT_ALL = "SELECT id, name, price, image_url FROM products"
    const val SELECT_BY_ID = "SELECT id, name, price, image_url FROM products WHERE id = ?"
    const val INSERT =
        "INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)"
    const val UPDATE_BY_ID = " UPDATE products SET name = ?, price = ?, image_url = ? WHERE id = ?"
    const val DELETE_BY_ID = "DELETE FROM products WHERE id = ?"
    const val COUNT_BY_ID = "SELECT count(*) FROM products WHERE id = ?"
}
