DELETE FROM cart_items;
DELETE FROM products;
DELETE FROM members WHERE email NOT IN ('user1@example.com', 'admin@example.com');

