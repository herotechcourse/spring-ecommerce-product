CREATE TABLE products
(
    id        LONG PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255),
    price     DECIMAL(10, 2),
    image_url VARCHAR(255)
);
