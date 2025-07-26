-- DROP TABLE product CASCADE;

CREATE TABLE product
(
    id       LONG         NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    price    DOUBLE       NOT NULL,
    imageUrl VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- DROP TABLE member CASCADE;

CREATE TABLE member
(
    id       LONG         NOT NULL AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
