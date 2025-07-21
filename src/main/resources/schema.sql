create table PRODUCTS
(
    id        INT AUTO_INCREMENT,
    name      VARCHAR(100) UNIQUE,
    price     DECIMAL(10, 2),
    image_url VARCHAR(500),
    PRIMARY KEY (id)
);
