-- Seed data for Module 11: products with inventory for placeOrder to decrement

INSERT INTO product (name, price, inventory) VALUES
('Lightweight Backpack',     79.99,  50),
('Stainless Water Bottle',   24.50,  100),
('Wireless Headphones',      129.00, 30),
('Mechanical Keyboard',      149.99, 20),
('Yoga Mat',                 45.00,  60),
('Hardcover Notebook',       18.50,  150),
('Cast Iron Skillet',        65.00,  25),
('Espresso Beans 1kg',       32.00,  80);

-- audit_log starts empty - the exercise generates entries
-- orders starts empty - the exercise generates entries