CREATE TABLE products
(
    id        LONG PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) UNIQUE NOT NULL,
    price     DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(2000)
);

CREATE TABLE members
(
    id       LONG PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);