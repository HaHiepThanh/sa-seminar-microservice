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
