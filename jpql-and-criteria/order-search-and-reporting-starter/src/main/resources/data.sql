-- Seed data for Module 8: 50 customers, 500 orders across two years (2025, 2026)

INSERT INTO customer (name, email)
SELECT
    'Customer ' || gs,
    'customer' || gs || '@udacishop.example'
FROM generate_series(1, 50) gs;

-- 500 orders spread across 2025 and 2026, varying statuses, varying amounts
INSERT INTO orders (customer_id, order_number, status, amount, placed_at)
SELECT
    1 + (gs % 50),                                                       -- customer_id 1-50
    'ORD-' || LPAD(gs::TEXT, 6, '0'),
    CASE (gs % 5)
        WHEN 0 THEN 'PLACED'
        WHEN 1 THEN 'PAID'
        WHEN 2 THEN 'SHIPPED'
        WHEN 3 THEN 'DELIVERED'
        ELSE 'CANCELLED'
    END,
    ROUND((random() * 500 + 10)::NUMERIC, 2),                            -- $10 - $510
    TIMESTAMP '2025-01-01' + (gs % 730) * INTERVAL '1 day'
                            + (random() * 86400) * INTERVAL '1 second'   -- spread across 2 years
FROM generate_series(1, 500) gs;