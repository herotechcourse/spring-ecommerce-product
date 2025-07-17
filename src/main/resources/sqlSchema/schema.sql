create table PRODUCTS
(
    ID       int              not null AUTO_INCREMENT,
    NAME     varchar(100)     not null,
    PRICE    double precision not null,
    IMAGEURL varchar(500),
    PRIMARY KEY (ID)
);
