CREATE TABLE products
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    price     DOUBLE NOT NULL,
    image_url VARCHAR(512) NOT NULL
);

CREATE TABLE members
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(50) DEFAULT '',
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50) DEFAULT 'USER'
);

CREATE TABLE cart_items
(
    member_id  BIGINT    NOT NULL,
    product_id BIGINT    NOT NULL,
    quantity   INT       NOT NULL CHECK (quantity > 0),
    added_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id, product_id),
    FOREIGN KEY (member_id) REFERENCES members (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);



