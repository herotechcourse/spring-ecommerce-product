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

CREATE TABLE IF NOT EXISTS cart_items (
    id          BIGINT      PRIMARY KEY AUTO_INCREMENT,
    member_id   VARCHAR(40) NOT NULL,
    product_id  BIGINT      NOT NULL,
    quantity    INT         NOT NULL,
    UNIQUE(member_id, product_id),
    FOREIGN KEY(member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);
