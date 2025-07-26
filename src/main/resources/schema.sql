-- schema.sql
CREATE TABLE IF NOT EXISTS products (
    id       BIGINT       PRIMARY KEY    AUTO_INCREMENT,
    name     VARCHAR      NOT NULL UNIQUE,
    price    DOUBLE       NOT NULL,
    img      VARCHAR(120) NOT NULL,
    quantity INTEGER      NOT NULL
);

CREATE TABLE IF NOT EXISTS members (
    user_id   BIGINT       PRIMARY KEY    AUTO_INCREMENT,
    user_name VARCHAR      NOT NULL,
    email    VARCHAR      NOT NULL UNIQUE,
    password_hash VARCHAR(120) NOT NULL,
    role VARCHAR      NOT NULL
);

CREATE TABLE IF NOT EXISTS carts (
    id           BIGINT     PRIMARY KEY    AUTO_INCREMENT,
    member_id     BIGINT     NOT NULL UNIQUE,
    created_at    TIMESTAMP  NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(user_id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS cart_events (
    id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id     BIGINT     NOT NULL,
    product_id BIGINT NOT NULL,
    quantity_added INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(user_id),
    FOREIGN KEY (product_id) REFERENCES products(id)
)
