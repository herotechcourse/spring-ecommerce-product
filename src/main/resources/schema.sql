CREATE TABLE products
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255),
    price     DECIMAL(10, 2),
    image_url VARCHAR(255)
);

CREATE TABLE members
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(320),
    password VARCHAR(8),
    role     VARCHAR(8),
    name     VARCHAR(50)
);

CREATE TABLE cart
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
