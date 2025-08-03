INSERT INTO products (id, name, price, image_url) VALUES
    (1, 'Apple', 1.0, 'https://apple.jpg'),
    (2, 'Banana', 1.2, 'https://banana.jpg'),
    (3, 'Orange', 1.3, 'https://orange.jpg'),
    (4, 'Mango', 2.0, 'https://mango.jpg'),
    (5, 'Grapes', 2.5, 'https://grapes.jpg'),
    (6, 'Kiwi', 3.0, 'https://kiwi.jpg');

INSERT INTO members (email, password) VALUES ('user@email.com', '1234');
INSERT INTO members (email, password) VALUES ('user2@email.com', '1234');
INSERT INTO members (email, password, role) VALUES ('admin@email.com', '1234', 'ADMIN');

INSERT INTO cart_items VALUES (1, 1, 3, DATEADD('DAY', -8, CURRENT_TIMESTAMP));

INSERT INTO cart_items VALUES (2, 1, 3, DATEADD('DAY', -10, CURRENT_TIMESTAMP));

INSERT INTO cart_items VALUES (3, 1, 3, DATEADD('DAY', -5, CURRENT_TIMESTAMP));

INSERT INTO cart_items VALUES (1, 2, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP));
INSERT INTO cart_items VALUES (2, 2, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP));
INSERT INTO cart_items VALUES (3, 2, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP));

INSERT INTO cart_items VALUES (1, 3, 1, DATEADD('DAY', -35, CURRENT_TIMESTAMP));

INSERT INTO cart_items VALUES (2, 4, 1, DATEADD('DAY', -3, CURRENT_TIMESTAMP));
INSERT INTO cart_items VALUES (3, 4, 2, DATEADD('DAY', -2, CURRENT_TIMESTAMP));

INSERT INTO cart_items VALUES (1, 5, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP));


