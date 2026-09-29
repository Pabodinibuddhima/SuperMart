-- =====================================================
-- SuperMart - Development / Demonstration Data
-- =====================================================

USE supermart_db;


-- Categories

INSERT INTO categories (name, description)
VALUES
    ('Dairy', 'Milk, cheese and other dairy products'),
    ('Bakery', 'Bread and bakery products'),
    ('Grocery', 'General grocery products'),
    ('Beverages', 'Drinks and beverages');


-- Suppliers

INSERT INTO suppliers (name, phone, email, address)
VALUES
    (
        'Fresh Foods Ltd',
        '0771234567',
        'sales@freshfoods.lk',
        'Colombo'
    ),
    (
        'Daily Distributors',
        '0712345678',
        'orders@dailydistributors.lk',
        'Colombo'
    );


-- Products

INSERT INTO products (
    barcode,
    name,
    category_id,
    supplier_id,
    cost_price,
    selling_price,
    quantity,
    reorder_level
)
VALUES
    (
        '100001',
        'Fresh Milk 1L',
        1,
        1,
        350.00,
        450.00,
        25,
        10
    ),
    (
        '100002',
        'White Bread',
        2,
        2,
        130.00,
        180.00,
        40,
        15
    ),
    (
        '100003',
        'Rice 5kg',
        3,
        2,
        1200.00,
        1500.00,
        8,
        10
    );
