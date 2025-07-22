CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR NOT NULL UNIQUE,
    description VARCHAR,
    price DOUBLE CHECK (price >= 0),
    image_url VARCHAR,
    quantity INT
);
