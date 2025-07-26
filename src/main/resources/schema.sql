CREATE TABLE products
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255),
    price     DECIMAL(10, 2),
    image_url VARCHAR(255)
);

CREATE TABLE members
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    email     VARCHAR(320),
    password  VARCHAR(8)
);
