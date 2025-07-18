CREATE TABLE IF NOT EXISTS products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price DOUBLE NOT NULL,
                          image_url VARCHAR(512) NOT NULL
);
