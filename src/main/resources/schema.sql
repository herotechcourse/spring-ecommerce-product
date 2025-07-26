-- schema.sql
CREATE TABLE IF NOT EXISTS products (
    id       BIGINT       PRIMARY KEY    AUTO_INCREMENT,
    name     VARCHAR      NOT NULL,
    price    DOUBLE       NOT NULL,
    img      VARCHAR(120) NOT NULL,
    quantity INTEGER      NOT NULL
    );

CREATE TABLE IF NOT EXISTS members (
    id          VARCHAR(40)     PRIMARY KEY,
    email       VARCHAR(255)    UNIQUE NOT NULL,
    password    VARCHAR(255)    NOT NULL,
    role        VARCHAR(255)    NOT NULL
   );
