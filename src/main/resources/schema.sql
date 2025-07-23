
DROP TABLE IF EXISTS member;
CREATE TABLE member
(
    id       LONG         NOT NULL AUTO_INCREMENT,
    email    varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email)
);


DROP TABLE IF EXISTS product;
CREATE TABLE product
(
    id       LONG         NOT NULL AUTO_INCREMENT,
    name     varchar(255) NOT NULL,
    price    DOUBLE       NOT NULL,
    imageUrl TEXT         NOT NULL,
    PRIMARY KEY (id)
);
