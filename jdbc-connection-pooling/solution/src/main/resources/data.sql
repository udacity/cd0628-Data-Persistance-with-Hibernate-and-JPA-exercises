-- Seed data for Module 1: 100 customers
-- The benchmark batch-inserts another 1,000 on top of this base set

INSERT INTO customer (email, first_name, last_name) VALUES
('emma.fischer@udacibank.example', 'Emma', 'Fischer'),
('liam.tanaka@udacibank.example', 'Liam', 'Tanaka'),
('aisha.okafor@udacibank.example', 'Aisha', 'Okafor'),
('noah.silva@udacibank.example', 'Noah', 'Silva'),
('mei.chen@udacibank.example', 'Mei', 'Chen'),
('oliver.bennett@udacibank.example', 'Oliver', 'Bennett'),
('zara.haddad@udacibank.example', 'Zara', 'Haddad'),
('lucas.morales@udacibank.example', 'Lucas', 'Morales'),
('hana.kim@udacibank.example', 'Hana', 'Kim'),
('amir.patel@udacibank.example', 'Amir', 'Patel');

-- Generate 90 more customers to bring base to 100
INSERT INTO customer (email, first_name, last_name)
SELECT
    'customer' || gs || '@udacibank.example',
    'First' || gs,
    'Last' || gs
FROM generate_series(11, 100) gs;