-- schema.sql
CREATE TABLE IF NOT EXISTS products (
    id       BIGINT       PRIMARY KEY    AUTO_INCREMENT,
    name     VARCHAR      NOT NULL,
    price    DOUBLE       NOT NULL,
    img      VARCHAR(120) NOT NULL,
    quantity INTEGER      NOT NULL
    );