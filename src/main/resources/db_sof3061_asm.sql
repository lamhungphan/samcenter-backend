# USE sof3061;

DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
                          id        INT AUTO_INCREMENT PRIMARY KEY,
                          username  VARCHAR(50)                            NOT NULL UNIQUE,
                          password  VARCHAR(255)                           NOT NULL,
                          email     VARCHAR(100)                           NOT NULL UNIQUE,
                          full_name VARCHAR(100),
                          phone     VARCHAR(15) UNIQUE,
                          address   VARCHAR(255),
                          role      ENUM ('CUSTOMER', 'STAFF', 'DIRECTOR') NOT NULL
);

CREATE TABLE categories (
                            category_id INT AUTO_INCREMENT PRIMARY KEY,
                            name        VARCHAR(255) NOT NULL
);

CREATE TABLE products (
                          id               INT AUTO_INCREMENT PRIMARY KEY,
                          name             VARCHAR(255)   NOT NULL,
                          quantity         INT            NOT NULL,
                          size             VARCHAR(50),
                          description      TEXT,
                          image            VARCHAR(255),
                          price            DECIMAL(10, 2) NOT NULL,
                          publish_date     DATE           NULL,
                          last_update_time DATETIME       NULL,
                          category_id      INT            NOT NULL,
                          FOREIGN KEY (category_id) REFERENCES categories (category_id) ON DELETE CASCADE
);

CREATE TABLE cart (
                      id         INT AUTO_INCREMENT PRIMARY KEY,
                      user_id    INT NOT NULL,
                      product_id INT NOT NULL,
                      quantity   INT NOT NULL,
                      FOREIGN KEY (user_id) REFERENCES accounts (id) ON DELETE CASCADE,
                      FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

CREATE TABLE orders (
                        id          INT AUTO_INCREMENT PRIMARY KEY,
                        user_id     INT            NOT NULL,
                        total_price DOUBLE         NOT NULL,
                        order_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        status      VARCHAR(255)   NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES accounts (id) ON DELETE CASCADE
);

CREATE TABLE order_details (
                               id         INT AUTO_INCREMENT PRIMARY KEY,
                               order_id   INT            NOT NULL,
                               product_id INT            NOT NULL,
                               quantity   INT            NOT NULL,
                               price      DECIMAL(10, 2) NOT NULL,
                               FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
                               FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

CREATE TABLE password_reset_token (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              token VARCHAR(255) NOT NULL,
                              account_id INT UNIQUE,
                              expiry_date DATETIME,
                              CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);


INSERT INTO accounts (username, password, email, full_name, phone, address, role)
VALUES
    ('director', '$2a$10$FKYl3UH2R5DwG.e6VE2UL.LQ2llAj/UCDRU50cEtTa80pz6irfbHu', 'admin@samsung.com', 'Admin Samsung', '0123456789', 'Hà Nội, Việt Nam', 'DIRECTOR'),
    ('staff1', '$2a$10$FKYl3UH2R5DwG.e6VE2UL.LQ2llAj/UCDRU50cEtTa80pz6irfbHu', 'staff1@samsung.com', 'Lê Minh Nhựt', '0912345678', 'Hà Nội, Việt Nam', 'STAFF'),
    ('user1', '$2a$10$gDlltTnwp.2bjYqsKJps6eEMG73HidFpmxJisr0H4jRE5qr73545y', 'user1@gmail.com', 'Nguyễn Văn A', '0987654321', 'Hồ Chí Minh, Việt Nam', 'CUSTOMER'),
    ('user2', '$2a$10$gDlltTnwp.2bjYqsKJps6eEMG73HidFpmxJisr0H4jRE5qr73545y', 'user2@gmail.com', 'Trần Thị B', '0971122334', 'Đà Nẵng, Việt Nam', 'CUSTOMER'),
    ('user3', '$2a$10$gDlltTnwp.2bjYqsKJps6eEMG73HidFpmxJisr0H4jRE5qr73545y', 'user3@gmail.com', 'Phạm Hữu C', '0965566778', 'Cần Thơ, Việt Nam', 'CUSTOMER');

INSERT INTO categories (name)
VALUES
    ('Điện thoại Samsung'),
    ('Máy tính bảng Samsung'),
    ('Đồng hồ Samsung'),
    ('Phụ kiện Samsung');

INSERT INTO products (name, quantity, size, description, image, price, publish_date, last_update_time, category_id)
VALUES
    ('Samsung Galaxy S24 Ultra', 50, NULL, 'Flagship Samsung 2024', 's24_ultra.jpg', 29990000, NOW(), NOW(), 1),
    ('Samsung Galaxy S23', 100, NULL, 'Điện thoại Samsung cao cấp', 's23.jpg', 19990000, NOW(), NOW(), 1),
    ('Samsung Galaxy S23 FE', 120, NULL, 'Bản tiết kiệm của S23', 's23fe.jpg', 14990000, NOW(), NOW(), 1),
    ('Samsung Galaxy Z Fold5', 30, NULL, 'Điện thoại màn gập mới nhất', 'zfold5.jpg', 40990000, NOW(), NOW(), 1),
    ('Samsung Galaxy Tab S9', 30, '12.4 inch', 'Máy tính bảng flagship Samsung', 'tab_s9.jpg', 24990000, NOW(), NOW(), 2),
    ('Samsung Galaxy Tab A8', 80, '10.5 inch', 'Máy tính bảng giá rẻ', 'tab_a8.jpg', 7990000, NOW(), NOW(), 2),
    ('Samsung Galaxy Watch 6', 60, '44mm', 'Smartwatch cao cấp Samsung', 'watch6.jpg', 8990000, NOW(), NOW(), 3),
    ('Samsung Galaxy Watch 5', 40, '40mm', 'Smartwatch phổ thông', 'watch5.jpg', 5990000, NOW(), NOW(), 3),
    ('Samsung Buds 2 Pro', 120, NULL, 'Tai nghe không dây cao cấp', 'buds2pro.jpg', 4990000, NOW(), NOW(), 4),
    ('Ốp lưng Galaxy S24 Ultra', 200, NULL, 'Ốp lưng bảo vệ điện thoại', 'oplung_s24.jpg', 500000, NOW(), NOW(), 4);

INSERT INTO cart (user_id, product_id, quantity)
VALUES
    (3, 1, 1),  -- user1 mua S24 Ultra
    (3, 8, 2),  -- user1 mua Watch 5
    (4, 5, 1),  -- user2 mua Tab S9
    (4, 10, 3), -- user2 mua Ốp lưng
    (5, 9, 1);  -- user3 mua Buds 2 Pro

INSERT INTO orders (user_id, total_price, order_date, status)
VALUES
    (3, 41980000, NOW(), 'PENDING'),  -- user1
    (4, 26490000, NOW(), 'SHIPPED'),  -- user2
    (5, 4990000, NOW(), 'COMPLETED'); -- user3

INSERT INTO order_details (order_id, product_id, quantity, price)
VALUES
    (1, 1, 1, 29990000), -- S24 Ultra
    (1, 8, 2, 5990000),  -- Watch 5
    (2, 5, 1, 24990000), -- Tab S9
    (2, 10, 3, 500000),  -- Ốp lưng
    (3, 9, 1, 4990000);  -- Buds 2 Pro


SELECT *
FROM accounts;

SELECT *
FROM categories;

SELECT *
FROM products;

SELECT *
FROM cart;

SELECT *
FROM orders;

SELECT *
FROM order_details;

SELECT * FROM password_reset_token;
