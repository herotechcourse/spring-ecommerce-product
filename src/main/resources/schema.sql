CREATE TABLE products
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) UNIQUE NOT NULL,
    price     DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(2000)
);

CREATE TABLE members
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS cart_items;
CREATE TABLE cart_items (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id   BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_member_product UNIQUE (member_id, product_id)
);
