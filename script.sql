-- DB: USER_SERVICE --
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_profiles (
    user_id CHAR(36) PRIMARY KEY,
    full_name VARCHAR(255),
    phone VARCHAR(20),
    avatar_url VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE addresses (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36),
    address_line VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    address_title VARCHAR(50),
    zip_code INT,
    is_default TINYINT(1) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);



-- DB: CATALOG_SERVICE --

CREATE TABLE categories (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(50)
);

CREATE TABLE products (
    id VARCHAR(10) PRIMARY KEY,
    category_id CHAR(36),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url TEXT,
    price DECIMAL(10,2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE product_attributes (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    product_id VARCHAR(10),
    attribute_name VARCHAR(50) NOT NULL,
    attribute_value VARCHAR(255) NOT NULL,
    attribute_type SMALLINT,
    FOREIGN KEY (product_id) REFERENCES products(id)
);


-- DB: INVENTORY_SERVICE --

CREATE TABLE inventory (
    product_id VARCHAR(10) PRIMARY KEY,
    quantity INT NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE inventory_logs (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    product_id VARCHAR(10),
    change_value INT,
    reason VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CHECK (reason IN ('ORDER', 'CANCEL', 'RESTOCK'))
);

CREATE TABLE group_buys (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    product_id VARCHAR(10),
    target_quantity INT NOT NULL,
    current_quantity INT DEFAULT 0,
    start_date DATETIME,
    end_date DATETIME,
    status VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CHECK (status IN ('OPEN', 'SUCCESS', 'FAILED'))
);


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


-- DB: CART_SERVICE --

CREATE TABLE cart_items (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36),
    product_id VARCHAR(10),
    quantity INT,
    UNIQUE KEY unique_user_product (user_id, product_id)
);

