-- =====================================================
-- SuperMart - Sales & Inventory Management System
-- Database Schema
-- =====================================================

CREATE DATABASE IF NOT EXISTS supermart_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE supermart_db;


-- =====================================================
-- CATEGORIES
-- =====================================================

CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================
-- SUPPLIERS
-- =====================================================

CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150),
    address VARCHAR(255),

    status ENUM('ACTIVE', 'INACTIVE')
        NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- PRODUCTS
-- =====================================================

CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    barcode VARCHAR(50) UNIQUE,
    name VARCHAR(150) NOT NULL,

    category_id INT NOT NULL,
    supplier_id INT,

    cost_price DECIMAL(10,2) NOT NULL,
    selling_price DECIMAL(10,2) NOT NULL,

    quantity INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 10,

    status ENUM('ACTIVE', 'INACTIVE')
        NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES categories(category_id),

    CONSTRAINT fk_product_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(supplier_id),

    CONSTRAINT chk_product_cost_price
        CHECK (cost_price >= 0),

    CONSTRAINT chk_product_selling_price
        CHECK (selling_price >= 0),

    CONSTRAINT chk_product_quantity
        CHECK (quantity >= 0),

    CONSTRAINT chk_product_reorder_level
        CHECK (reorder_level >= 0)
);

-- =====================================================
-- STOCK_TRANSACTIONS
-- =====================================================


CREATE TABLE IF NOT EXISTS stock_transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    transaction_type ENUM(
        'STOCK_IN',
        'SALE',
        'RETURN',
        'DAMAGE',
        'ADJUSTMENT'
    ) NOT NULL,
    quantity INT NOT NULL,
    reference_note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_stock_transaction_product
        FOREIGN KEY (product_id)
        REFERENCES products(product_id),

    CONSTRAINT chk_stock_transaction_quantity
        CHECK (quantity > 0)
);

-- =====================================================
-- CUSTOMERS
-- =====================================================


CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150),
    address VARCHAR(255),
    status ENUM('ACTIVE', 'INACTIVE')
        NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_customer_phone
        UNIQUE (phone),

    CONSTRAINT uq_customer_email
        UNIQUE (email)
);

-- =====================================================
-- EMPLOYEES
-- =====================================================

CREATE TABLE IF NOT EXISTS employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150),

    role ENUM(
        'MANAGER',
        'CASHIER',
        'STOCK_CLERK'
    ) NOT NULL,

    status ENUM(
        'ACTIVE',
        'INACTIVE'
    ) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_employee_email
        UNIQUE (email)
);


-- =====================================================
-- SALES
-- =====================================================

CREATE TABLE IF NOT EXISTS sales (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,

    invoice_number VARCHAR(50)
        NOT NULL UNIQUE,

    customer_id INT,
    employee_id INT,

    subtotal DECIMAL(10,2)
        NOT NULL,

    discount DECIMAL(10,2)
        NOT NULL DEFAULT 0.00,

    total_amount DECIMAL(10,2)
        NOT NULL,

    status ENUM(
        'COMPLETED',
        'REFUNDED',
        'PARTIALLY_REFUNDED'
    ) NOT NULL DEFAULT 'COMPLETED',

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sale_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(customer_id),

    CONSTRAINT fk_sale_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id),

    CONSTRAINT chk_sale_subtotal
        CHECK (subtotal >= 0),

    CONSTRAINT chk_sale_discount
        CHECK (discount >= 0),

    CONSTRAINT chk_sale_total
        CHECK (total_amount >= 0)
);


-- =====================================================
-- SALE ITEMS
-- =====================================================

CREATE TABLE IF NOT EXISTS sale_items (
    sale_item_id INT AUTO_INCREMENT PRIMARY KEY,

    sale_id INT NOT NULL,
    product_id INT NOT NULL,

    quantity INT NOT NULL,

    unit_price DECIMAL(10,2)
        NOT NULL,

    line_total DECIMAL(10,2)
        NOT NULL,

    CONSTRAINT fk_sale_item_sale
        FOREIGN KEY (sale_id)
        REFERENCES sales(sale_id),

    CONSTRAINT fk_sale_item_product
        FOREIGN KEY (product_id)
        REFERENCES products(product_id),

    CONSTRAINT chk_sale_item_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_sale_item_unit_price
        CHECK (unit_price >= 0),

    CONSTRAINT chk_sale_item_line_total
        CHECK (line_total >= 0)
);


-- =====================================================
-- PAYMENTS
-- =====================================================

CREATE TABLE IF NOT EXISTS payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,

    sale_id INT NOT NULL,

    payment_method ENUM(
        'CASH',
        'CARD'
    ) NOT NULL,

    amount DECIMAL(10,2)
        NOT NULL,

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_sale
        FOREIGN KEY (sale_id)
        REFERENCES sales(sale_id),

    CONSTRAINT chk_payment_amount
        CHECK (amount >= 0)
);