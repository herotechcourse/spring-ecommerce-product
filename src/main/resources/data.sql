INSERT INTO product (name, price, imageUrl) VALUES ('Iron Man', 1000, 'https://alexnsan.comics/imageurl/1');
INSERT INTO product (name, price, imageUrl) VALUES ('X-men', 1000, 'https://alexnsan.comics/imageurl/2');
INSERT INTO product (name, price, imageUrl) VALUES ('Superman', 1000, 'https://alexnsan.comics/imageurl/3');
INSERT INTO product (name, price, imageUrl) VALUES ('Naruto', 1000, 'https://alexnsan.comics/imageurl/4');
INSERT INTO product (name, price, imageUrl) VALUES ('Full Metal Alchemist', 1000, 'https://alexnsan.comics/imageurl/5');
INSERT INTO product (name, price, imageUrl) VALUES ('Batman', 1000, 'https://alexnsan.comics/imageurl/6');
;

INSERT INTO member (email, password) VALUES ( 'san@htc.com', 'san1234');
INSERT INTO member (email, password) VALUES ( 'dan@htc.com', 'dan1234');
INSERT INTO member (email, password) VALUES ( 'ann@htc.com', 'ann1234');
INSERT INTO member (email, password) VALUES ( 'min@htc.com', 'min1234');
;

-- Cart Item Events with varying timestamps
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (1, 1, CURRENT_TIMESTAMP - INTERVAL '5' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (1, 2, CURRENT_TIMESTAMP - INTERVAL '3' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (2, 2, CURRENT_TIMESTAMP - INTERVAL '2' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (3, 2, CURRENT_TIMESTAMP - INTERVAL '1' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (1, 3, CURRENT_TIMESTAMP - INTERVAL '10' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (2, 3, CURRENT_TIMESTAMP - INTERVAL '20' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (3, 3, CURRENT_TIMESTAMP - INTERVAL '25' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (1, 4, CURRENT_TIMESTAMP - INTERVAL '6' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (2, 5, CURRENT_TIMESTAMP - INTERVAL '7' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (3, 5, CURRENT_TIMESTAMP - INTERVAL '8' DAY);
INSERT INTO cart_item_event (member_id, product_id, created_at) VALUES (1, 5, CURRENT_TIMESTAMP - INTERVAL '9' DAY);
;