TRUNCATE TABLE cart_items;

INSERT INTO cart_items (member_id, product_id, quantity, created_at) VALUES
        ((SELECT id FROM members WHERE email = 'user1@example.com'), 1, 2, NOW()),
        ((SELECT id FROM members WHERE email = 'user2@example.com'), 2, 1, NOW()),
        ((SELECT id FROM members WHERE email = 'user1@example.com'), 2, 3, NOW());
