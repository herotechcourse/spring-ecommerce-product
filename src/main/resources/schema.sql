CREATE TABLE products
(
    id        LONG PRIMARY KEY AUTO_INCREMENT UNIQUE,
    name      VARCHAR(255) UNIQUE,
    price     DECIMAL(10, 2),
    image_url VARCHAR(255)
);
