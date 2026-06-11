-- Seed data for Module 4

INSERT INTO blog_post (title, body) VALUES
('Why N+1 matters', 'A short note about query patterns and lazy loading.'),
('Connection pooling 101', 'How HikariCP makes JDBC less painful.'),
('Optimistic vs pessimistic locking', 'When to choose which.'),
('Understanding @Transactional', 'Common pitfalls in Spring transactions.'),
('Native queries in Hibernate', 'When JPQL is not enough.');

INSERT INTO comment (post_id, author, body) VALUES
(1, 'Liam',  'Great explanation, thanks.'),
(1, 'Aisha', 'I had this exact problem last week.'),
(1, 'Noah',  'Saved my afternoon — thank you.'),
(2, 'Mei',   'Could you cover pool sizing in a follow-up?'),
(2, 'Emma',  'Bookmarking this.'),
(3, 'Oliver','Pessimistic locking is underrated.'),
(4, 'Zara',  'The proxy bypass gotcha is real.'),
(5, 'Lucas', 'Constructor projections changed my life.');

INSERT INTO student (name, email) VALUES
('Emma Fischer',   'emma.fischer@udaciuniversity.example'),
('Liam Tanaka',    'liam.tanaka@udaciuniversity.example'),
('Aisha Okafor',   'aisha.okafor@udaciuniversity.example'),
('Noah Silva',     'noah.silva@udaciuniversity.example'),
('Mei Chen',       'mei.chen@udaciuniversity.example');

INSERT INTO course (code, title, credits) VALUES
('CS101', 'Introduction to Computer Science', 3),
('CS210', 'Data Structures and Algorithms',   4),
('CS340', 'Database Systems',                 4),
('CS415', 'Concurrent Programming',           3),
('CS450', 'Software Engineering',             3);

INSERT INTO enrollment (student_id, course_id, enrollment_date) VALUES
(1, 1, '2026-01-15'),
(1, 2, '2026-01-15'),
(1, 3, '2026-09-01'),
(2, 1, '2026-01-15'),
(2, 3, '2026-09-01'),
(3, 1, '2026-01-15'),
(3, 4, '2026-09-01'),
(3, 5, '2026-09-01'),
(4, 2, '2026-01-15'),
(5, 3, '2026-09-01'),
(5, 5, '2026-09-01');