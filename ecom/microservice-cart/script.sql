-- CART SERVICE --

CREATE TABLE cart_items (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36),
    product_id VARCHAR(10),
    quantity INT,
    UNIQUE KEY unique_user_product (user_id, product_id)
);

-- Mặc định khởi tạo Giỏ hàng mẫu cho sếp --
INSERT INTO cart_items (user_id, product_id, quantity) VALUES ('1', 'GMK_BOT', 1);
INSERT INTO cart_items (user_id, product_id, quantity) VALUES ('1', 'GAT_INK', 3);
INSERT INTO cart_items (user_id, product_id, quantity) VALUES ('2', 'HOLY_PND', 2);
INSERT INTO cart_items (user_id, product_id, quantity) VALUES ('2', 'KBD_67L', 1);
INSERT INTO cart_items (user_id, product_id, quantity) VALUES ('3', 'ZAKU_L', 9);
INSERT INTO cart_items (user_id, product_id, quantity) VALUES ('4', 'TX_STAB', 2);