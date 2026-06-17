-- Seed data for Module 3

INSERT INTO app_user (username, email) VALUES
('emma_f',  'emma.fischer@udacishop.example'),
('liam_t',  'liam.tanaka@udacishop.example'),
('aisha_o', 'aisha.okafor@udacishop.example');

INSERT INTO user_profile (id, display_name, bio) VALUES
(1, 'Emma F.',  'Long-time customer, loves outdoor gear'),
(2, 'Liam T.',  'New shopper'),
(3, 'Aisha O.', 'Buys mostly home goods');

INSERT INTO customer (name, email) VALUES
('Emma Fischer',  'emma.fischer@udacishop.example'),
('Liam Tanaka',   'liam.tanaka@udacishop.example'),
('Aisha Okafor',  'aisha.okafor@udacishop.example'),
('Noah Silva',    'noah.silva@udacishop.example'),
('Mei Chen',      'mei.chen@udacishop.example');

INSERT INTO orders (order_number, customer_id, placed_at) VALUES
('ORD-1001', 1, '2026-06-01 10:15:00'),
('ORD-1002', 1, '2026-06-03 14:22:00'),
('ORD-1003', 2, '2026-06-05 09:30:00'),
('ORD-1004', 3, '2026-06-06 11:45:00'),
('ORD-1005', 3, '2026-06-07 16:10:00'),
('ORD-1006', 4, '2026-06-08 08:00:00'),
('ORD-1007', 5, '2026-06-09 13:30:00'),
('ORD-1008', 5, '2026-06-10 19:00:00');