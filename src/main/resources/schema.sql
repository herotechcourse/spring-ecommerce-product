create table PRODUCTS
(
    ID        int          not null AUTO_INCREMENT,
    NAME      varchar(100) not null,
    PRICE double not null,
    IMAGE_URL varchar(500),
    PRIMARY KEY (ID)
);

CREATE TABLE MEMBERS
(
    ID       INT AUTO_INCREMENT PRIMARY KEY,
    EMAIL    VARCHAR(100) NOT NULL UNIQUE,
    PASSWORD VARCHAR(100) NOT NULL
);
