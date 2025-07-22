create table PRODUCTS
(
    id        int          not null AUTO_INCREMENT,
    name      varchar(100) not null,
    price     DECIMAL(10, 2),
    image_url varchar(500) not null,
    PRIMARY KEY (id)
);
