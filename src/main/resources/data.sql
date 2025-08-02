INSERT INTO members (email, password, role, name)
VALUES ('admin@test.com', '1234', 'ADMIN', 'Jon'),
       ('user@test.com', '1234', 'USER', 'Ann' );

INSERT INTO products (name, price, image_url)
VALUES ('Flat White', 12.99, 'https://example.com/images/espresso-beans.jpg'),
       ('French Press', 24.95, 'https://example.com/images/french-press.jpg'),
       ('Coffee Filter', 6.75, 'https://example.com/images/coffee-filter.jpg');

INSERT INTO cart (member_id, product_id, quantity, created_at, updated_at)
VALUES (1, 1, 2, '2025-07-27 10:00:00', '2025-07-27 10:00:00'), -- 2 x Flat White
       ( 1, 2, 1, '2025-05-27 9:00:00', '2025-05-27 9:00:00'), -- 1 x French Press
       (1,3, 3, '2025-07-27 2:00:00', '2025-07-27 2:00:00'); -- 3 x Coffee Filter
