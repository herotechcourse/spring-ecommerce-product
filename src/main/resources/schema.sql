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

create table CARTS
(
    cart_id        INT AUTO_INCREMENT,
    user_id   INT UNIQUE,
    PRIMARY KEY (cart_id),
    FOREIGN KEY (user_id) REFERENCES MEMBERS(id)
);

create table CART_ITEMS (
    cart_id INT,
    product_id INT,
    quantity INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_id, product_id),
    FOREIGN KEY (cart_id) REFERENCES CARTS(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES PRODUCTS(id)
)
