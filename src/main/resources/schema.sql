CREATE TABLE products (
    id        INT NOT NULL AUTO_INCREMENT,
    name      VARCHAR(100) NOT NULL,
    price     DOUBLE NOT NULL,
    image_url VARCHAR(500),
    PRIMARY KEY (id)
);

CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
