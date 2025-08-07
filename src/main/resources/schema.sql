DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS product;

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    email   VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name    VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE product
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    price    DOUBLE       NOT NULL,
    imageUrl TEXT         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE cart
(
    id        BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (member_id),
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);

CREATE TABLE cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    cart_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    added_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity   INT    NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (cart_id) REFERENCES cart (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    UNIQUE (cart_id, product_id) -- Only one entry per product per cart
);
