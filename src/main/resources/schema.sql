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

-- DROP TABLE cart_item CASCADE;

CREATE TABLE cart_item
(
    id         LONG NOT NULL AUTO_INCREMENT,
    member_id  LONG NOT NULL,
    product_id LONG NOT NULL,
    quantity   INT DEFAULT 1,
    PRIMARY KEY (id),

    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE cart_item_event
(
    id         LONG AUTO_INCREMENT,
    member_id  LONG NOT NULL,
    product_id LONG NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),

    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

// check for how to creat index!
