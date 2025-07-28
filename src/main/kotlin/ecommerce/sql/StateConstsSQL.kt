package ecommerce.sql

object StateConstsSQL {
    const val TOP_ADDED_PRODUCTS_TEMPLATE = """
        SELECT 
            p.id AS product_id,
            COUNT(*) AS add_count,
            MAX(c.created_at) AS last_added_at
        FROM cart_items c
        JOIN products p ON c.product_id = p.id
        WHERE c.created_at >= DATE_SUB(NOW(), INTERVAL ? DAY)
        GROUP BY c.product_id, p.id
        ORDER BY %s
        LIMIT ?
        """

    const val ALL_ACTIVE_MEMBERS_TEMPLATE = """
        SELECT DISTINCT m.id AS member_id, m.name, m.email
        FROM members m
        WHERE EXISTS (
            SELECT 1 FROM cart_items c
            WHERE c.member_id = m.id
            AND c.created_at >= DATEADD('DAY', ?, CURRENT_TIMESTAMP)
        )
        ORDER BY %s
        """

    const val ACTIVE_MEMBERS_TEMPLATE = """
        SELECT 
            m.id AS member_id,
            m.email,
            COUNT(c.id) AS add_count
        FROM members m
        JOIN cart_items c ON c.member_id = m.id
        WHERE c.created_at >= DATEADD('DAY', ?, CURRENT_TIMESTAMP)
        GROUP BY m.id, m.email
        ORDER BY add_count DESC
        LIMIT ?
        """
}
