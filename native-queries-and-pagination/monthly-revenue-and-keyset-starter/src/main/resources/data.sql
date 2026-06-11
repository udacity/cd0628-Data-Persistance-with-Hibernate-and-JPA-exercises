-- Seed data for Module 9
-- 10,000 products with sortable names; orders across 12 months × 5 categories

INSERT INTO product (name, category, price)
SELECT
    'Product ' || LPAD(gs::TEXT, 5, '0'),
    CASE (gs % 5)
        WHEN 0 THEN 'outdoor'
        WHEN 1 THEN 'electronics'
        WHEN 2 THEN 'kitchen'
        WHEN 3 THEN 'fitness'
        ELSE 'office'
    END,
    ROUND((random() * 200 + 5)::NUMERIC, 2)
FROM generate_series(1, 10000) gs;

-- 12 months of orders, 5 categories, ~30 orders per category per month = 1,800 orders
INSERT INTO orders (category, amount, placed_at)
SELECT
    CASE (gs % 5)
        WHEN 0 THEN 'outdoor'
        WHEN 1 THEN 'electronics'
        WHEN 2 THEN 'kitchen'
        WHEN 3 THEN 'fitness'
        ELSE 'office'
    END,
    ROUND((random() * 300 + 20)::NUMERIC, 2),
    TIMESTAMP '2025-06-01' + (gs % 365) * INTERVAL '1 day'
                          + (random() * 86400) * INTERVAL '1 second'
FROM generate_series(1, 1800) gs;