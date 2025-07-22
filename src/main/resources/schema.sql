create table PRODUCTS
(
    ID       int              not null AUTO_INCREMENT,
    NAME     varchar(100)     not null,
    PRICE    double not null,
    IMAGE_URL varchar(500),
    PRIMARY KEY (ID)
);
