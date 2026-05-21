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

-- SAMPLE DATA --
INSERT INTO inventory (product_id, quantity) VALUES 
('p01', 100),
('p02', 150),
('p03', 200),
('p04', 50),
('p05', 300),
('p06', 120),
('p07', 80),
('p08', 45);

INSERT INTO group_buys (product_id, target_quantity, current_quantity, start_date, end_date, status) VALUES 
('p01', 50, 10, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 'OPEN');

