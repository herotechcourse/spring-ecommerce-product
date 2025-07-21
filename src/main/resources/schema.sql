CREATE TABLE products (
    id        INT NOT NULL AUTO_INCREMENT,
    name      VARCHAR(100) NOT NULL,
    price     DOUBLE NOT NULL,
    image_url VARCHAR(500),
    PRIMARY KEY (id)
);
