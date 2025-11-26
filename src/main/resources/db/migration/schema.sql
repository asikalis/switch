-- =====================================================================
--  SCHEMA: Switch Smart Billing & Inventory Management
--  DATABASE: MySQL 8.x
--  AUTHOR: GPT-5
--  DESCRIPTION: Schema for managing multiple businesses, customers,
--               products, invoices, and statistics.
-- =====================================================================

-- -----------------------------------------------------
-- Drop existing tables (for development resets)
-- -----------------------------------------------------
DROP TABLE IF EXISTS statistics;
DROP TABLE IF EXISTS invoice_items;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS businesses;
DROP TABLE IF EXISTS users;

-- -----------------------------------------------------
-- Table: users
-- -----------------------------------------------------
CREATE TABLE users (
                       id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username        VARCHAR(50) NOT NULL UNIQUE,
                       password        VARCHAR(255) NOT NULL,
                       full_name       VARCHAR(100),
                       email           VARCHAR(100) UNIQUE,
                       role            ENUM('ADMIN', 'OWNER', 'STAFF') DEFAULT 'OWNER',
                       created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------
-- Table: businesses
-- -----------------------------------------------------
CREATE TABLE businesses (
                            id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id         BIGINT NOT NULL,
                            name            VARCHAR(100) NOT NULL,
                            type            VARCHAR(50),
                            address         VARCHAR(255),
                            phone           VARCHAR(20),
                            email           VARCHAR(100),
                            gst_number      VARCHAR(50),
                            logo_url        VARCHAR(255),
                            created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            CONSTRAINT fk_business_user FOREIGN KEY (user_id)
                                REFERENCES users(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table: customers
-- -----------------------------------------------------
CREATE TABLE customers (
                           id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                           business_id     BIGINT NOT NULL,
                           name            VARCHAR(100) NOT NULL,
                           phone           VARCHAR(20),
                           email           VARCHAR(100),
                           address         VARCHAR(255),
                           created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_customer_business FOREIGN KEY (business_id)
                               REFERENCES businesses(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table: products
-- -----------------------------------------------------
CREATE TABLE products (
                          id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                          business_id     BIGINT NOT NULL,
                          name            VARCHAR(100) NOT NULL,
                          category        VARCHAR(50),
                          unit_price      DECIMAL(10,2) NOT NULL,
                          cost_price      DECIMAL(10,2),
                          stock_qty       INT DEFAULT 0,
                          unit            VARCHAR(20),
                          barcode         VARCHAR(50),
                          created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_product_business FOREIGN KEY (business_id)
                              REFERENCES businesses(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table: invoices
-- -----------------------------------------------------
CREATE TABLE invoices (
                          id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                          business_id     BIGINT NOT NULL,
                          customer_id     BIGINT,
                          invoice_number  VARCHAR(50) NOT NULL UNIQUE,
                          total_amount    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                          tax_amount      DECIMAL(10,2) DEFAULT 0.00,
                          discount        DECIMAL(10,2) DEFAULT 0.00,
                          net_amount      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                          invoice_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          payment_mode    VARCHAR(20) DEFAULT 'CASH',
                          status          ENUM('PAID', 'UNPAID', 'CANCELLED') DEFAULT 'PAID',
                          CONSTRAINT fk_invoice_business FOREIGN KEY (business_id)
                              REFERENCES businesses(id) ON DELETE CASCADE,
                          CONSTRAINT fk_invoice_customer FOREIGN KEY (customer_id)
                              REFERENCES customers(id) ON DELETE SET NULL
);

-- -----------------------------------------------------
-- Table: invoice_items
-- -----------------------------------------------------
CREATE TABLE invoice_items (
                               id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                               invoice_id      BIGINT NOT NULL,
                               product_id      BIGINT NOT NULL,
                               quantity        INT NOT NULL DEFAULT 1,
                               unit_price      DECIMAL(10,2) NOT NULL,
                               total_price     DECIMAL(10,2) NOT NULL,
                               CONSTRAINT fk_item_invoice FOREIGN KEY (invoice_id)
                                   REFERENCES invoices(id) ON DELETE CASCADE,
                               CONSTRAINT fk_item_product FOREIGN KEY (product_id)
                                   REFERENCES products(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table: statistics (Optional - cached analytics)
-- -----------------------------------------------------
CREATE TABLE statistics (
                            id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                            business_id     BIGINT NOT NULL,
                            date            DATE NOT NULL,
                            total_sales     DECIMAL(10,2) DEFAULT 0.00,
                            total_invoices  INT DEFAULT 0,
                            top_product_id  BIGINT,
                            CONSTRAINT fk_stats_business FOREIGN KEY (business_id)
                                REFERENCES businesses(id) ON DELETE CASCADE,
                            CONSTRAINT fk_stats_product FOREIGN KEY (top_product_id)
                                REFERENCES products(id) ON DELETE SET NULL
);

-- -----------------------------------------------------
-- Indexes for performance
-- -----------------------------------------------------
CREATE INDEX idx_business_user ON businesses(user_id);
CREATE INDEX idx_customer_business ON customers(business_id);
CREATE INDEX idx_product_business ON products(business_id);
CREATE INDEX idx_invoice_business ON invoices(business_id);
CREATE INDEX idx_invoice_customer ON invoices(customer_id);
CREATE INDEX idx_item_invoice ON invoice_items(invoice_id);
CREATE INDEX idx_stats_business ON statistics(business_id);

-- -----------------------------------------------------
-- Sample default user (for testing)
-- -----------------------------------------------------
INSERT INTO users (username, password, full_name, email, role)
VALUES ('admin', '{noop}admin123', 'System Admin', 'admin@switch.com', 'ADMIN');
