INSERT INTO products (name, price, image_url)
VALUES ('Cola', 2.52, 'https://t4.ftcdn.net/jpg/02/84/65/61/360_F_284656117_sPF8gVWaX627bq5qKrlrvCz1eFfowdBf.jpg');
INSERT INTO products (name, price, image_url)
VALUES ('Fanta', 2.49,
        'https://media.istockphoto.com/id/493952763/photo/can-of-fanta-orange.jpg?s=612x612&w=0&k=20&c=NcwCzRXwfB5jghmlAMDW-I26pNVa6UKtwuCfeJk5YEo=');
INSERT INTO products (name, price, image_url)
VALUES ('Coffee', 3.19, 'https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG');
INSERT INTO products (name, price, image_url)
VALUES ('Sprite', 2.19, 'https://tukwila.de/wp-content/uploads/2020/01/sprite-1lt-222x300.jpg');
INSERT INTO products (name, price, image_url)
VALUES ('Ice Cream', 1.34, 'https://cdn.loveandlemons.com/wp-content/uploads/2025/05/chocolate-ice-cream.jpg');
INSERT INTO products (name, price, image_url)
VALUES ('Pepsi', 2.54, 'https://superalko.de/5340-superlarge_default/pepsi-cola-24x033cl.jpg');

INSERT INTO CART_ITEMS (member_id, product_id, added_at)
VALUES (1, 1, '2025-07-24 10:00:00');
INSERT INTO CART_ITEMS (member_id, product_id, added_at)
VALUES (1, 2, '2025-07-23 12:00:00');
INSERT INTO CART_ITEMS (member_id, product_id, added_at)
VALUES (1, 3, '2025-07-22 15:00:00');
INSERT INTO CART_ITEMS (member_id, product_id, added_at)
VALUES (2, 1, '2025-07-20 09:00:00');
INSERT INTO CART_ITEMS (member_id, product_id, added_at)
VALUES (2, 2, '2025-07-19 14:00:00');
INSERT INTO CART_ITEMS (member_id, product_id, added_at)
VALUES (2, 4, '2025-07-18 11:00:00');