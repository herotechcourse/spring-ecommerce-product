create table PRODUCTS
(
    id        INT AUTO_INCREMENT,
    name      VARCHAR(100) UNIQUE,
    price     DECIMAL(10, 2),
    image_url VARCHAR(500),
    PRIMARY KEY (id)
);

create table MEMBERS
(
    id        INT AUTO_INCREMENT,
    email     VARCHAR(20) UNIQUE,
    password  VARCHAR(50),
    role      VARCHAR(10),
    PRIMARY KEY (id)
);
