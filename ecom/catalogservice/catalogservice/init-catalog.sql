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

-- SAMPLE DATA --
INSERT INTO categories (id, name) VALUES 
('cat-1', 'Smartphones'), 
('cat-2', 'Smartwatches'),
('cat-3', 'Accessories');

INSERT INTO products (id, category_id, name, description, image_url, price) VALUES 
('p01', 'cat-1', 'iPhone 15 Pro Max', 'Apple iPhone 15 Pro Max 256GB', 'cart-item1.jpg', 1199.00),
('p02', 'cat-1', 'Samsung Galaxy S24 Ultra', 'Samsung Galaxy S24 Ultra 512GB', 'insta-item1.jpg', 1299.00),
('p03', 'cat-2', 'Apple Watch Series 9', 'Apple Watch Series 9 GPS 45mm', 'product-item3.jpg', 399.00),
('p04', 'cat-2', 'Galaxy Watch 6 Classic', 'Samsung Galaxy Watch 6 Classic 47mm', 'cart-item2.jpg', 349.00),
('p05', 'cat-3', 'AirPods Pro 2', 'Apple AirPods Pro (2nd Generation)', 'insta-item2.jpg', 249.00),
('p06', 'cat-3', 'Galaxy Buds 2 Pro', 'Samsung Galaxy Buds 2 Pro', 'product-item6.jpg', 199.00),
('p07', 'cat-1', 'Google Pixel 8 Pro', 'Google Pixel 8 Pro 128GB', 'insta-item3.jpg', 999.00),
('p08', 'cat-3', 'Sony WH-1000XM5', 'Sony Wireless Noise Canceling Headphones', 'product-item8.jpg', 398.00);
