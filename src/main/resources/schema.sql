CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    price DOUBLE CHECK (price >= 0),
    image_url VARCHAR(255)
);
