-- DB: ORDER_SERVICE --
CREATE TABLE orders (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36),
    phone VARCHAR(20),
    status VARCHAR(20),
    total_price DECIMAL(10,2),
    address_line VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CHECK (status IN ('PENDING', 'PAID', 'CANCELLED'))
);

CREATE TABLE order_items (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    order_id CHAR(36),
    product_id VARCHAR(10),
    product_name VARCHAR(255),
    unit_price DECIMAL(10,2),
    quantity INT,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE payments (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    order_id CHAR(36),
    payment_method VARCHAR(50),
    status VARCHAR(20),
    paid_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    CHECK (status IN ('SUCCESS', 'FAILED'))
);
