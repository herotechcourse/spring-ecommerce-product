DROP TABLE cart_item_event CASCADE;

CREATE TABLE cart_item_event
(
    id         BIGINT AUTO_INCREMENT,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),

    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

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
