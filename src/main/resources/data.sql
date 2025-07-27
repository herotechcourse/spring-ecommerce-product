INSERT INTO members (email, password, role)
VALUES ('admin@test.com', '1234', 'ADMIN'),
       ('user@test.com', '1234', 'USER');

INSERT INTO products (name, price, image_url)
VALUES ('Flat White', 12.99, 'https://example.com/images/espresso-beans.jpg'),
       ('French Press', 24.95, 'https://example.com/images/french-press.jpg'),
       ('Coffee Filter', 6.75, 'https://example.com/images/coffee-filter.jpg');

INSERT INTO cart (member_id, product_id, quantity)
VALUES (1, 1, 2), -- 2 x Flat White
       (1, 2, 1), -- 1 x French Press
       (1, 4, 3); -- 3 x Coffee Filter
