-- DB: CART_SERVICE --
CREATE TABLE cart_items (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36),
    product_id VARCHAR(10),
    quantity INT,
    UNIQUE KEY unique_user_product (user_id, product_id)
);
