DROP TABLE cart_item CASCADE;

CREATE TABLE cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT DEFAULT 1,
    PRIMARY KEY (id),

    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);
