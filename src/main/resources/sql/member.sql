DROP TABLE member CASCADE;

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO member (email, password, role) VALUES ( 'san@htc.com', 'san1234', 'admin');
INSERT INTO member (email, password, role) VALUES ( 'dan@htc.com', 'dan1234', 'admin');
INSERT INTO member (email, password) VALUES ( 'ann@htc.com', 'ann1234');
INSERT INTO member (email, password) VALUES ( 'min@htc.com', 'min1234');
