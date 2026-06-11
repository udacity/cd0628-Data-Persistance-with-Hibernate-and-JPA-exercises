-- Seed data for Module 10: 200 products across 5 categories, 1,000 order items

INSERT INTO product (name, category, price, in_stock)
SELECT
    'Product ' || LPAD(gs::TEXT, 4, '0'),
    CASE (gs % 5)
        WHEN 0 THEN 'outdoor'
        WHEN 1 THEN 'electronics'
        WHEN 2 THEN 'kitchen'
        WHEN 3 THEN 'fitness'
        ELSE 'office'
    END,
    ROUND((random() * 200 + 5)::NUMERIC, 2),
    (random() > 0.1)                  -- ~90% in stock
FROM generate_series(1, 200) gs;

INSERT INTO order_item (product_id, quantity, sold_at)
SELECT
    1 + (gs % 200),
    1 + (gs % 5),
    TIMESTAMP '2026-01-01' + (gs % 180) * INTERVAL '1 day'
                          + (random() * 86400) * INTERVAL '1 second'
FROM generate_series(1, 1000) gs;