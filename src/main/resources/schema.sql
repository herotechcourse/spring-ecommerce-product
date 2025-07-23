-- schema.sql
CREATE TABLE IF NOT EXISTS products (
    id       BIGINT       PRIMARY KEY    AUTO_INCREMENT,
    name     VARCHAR      NOT NULL UNIQUE,
    price    DOUBLE       NOT NULL,
    img      VARCHAR(120) NOT NULL,
    quantity INTEGER      NOT NULL
    );

CREATE TABLE IF NOT EXISTS members (
    userId   BIGINT       PRIMARY KEY    AUTO_INCREMENT,
    userName VARCHAR      NOT NULL,
    email    VARCHAR      NOT NULL UNIQUE,
    passwordHash VARCHAR(120) NOT NULL,
    role VARCHAR      NOT NULL
);